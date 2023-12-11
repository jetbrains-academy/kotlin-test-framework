package org.jetbrains.academy.test.system.kotlin.ij.analyzer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentsOfType
import org.jetbrains.academy.test.system.ij.formatting.formatting
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.*
import org.junit.jupiter.api.Assertions

/** Extracts [kotlin elements][KtElement] of given type from kotlin related files in project. */
fun <T : PsiElement> PsiElement.extractElementsOfTypes(vararg psiElementClass: Class<out T>): MutableCollection<T> =
    psiElementClass.flatMap { PsiTreeUtil.collectElementsOfType(this, it) }.toMutableList()

/**
 * Retrieves the constant value of a property, if it is declared as a constant.
 *
 * @return The constant value of the property as a string, or null if the property is not constant or has no value.
 * @throws IllegalStateException if the constant property contains more than one value.
 */
private fun KtProperty.getConstValue(): String? {
    val possibleValue =
        extractElementsOfTypes(KtConstantExpression::class.java, KtStringTemplateExpression::class.java)
    if (possibleValue.isEmpty()) {
        return null
    }
    Assertions.assertEquals(possibleValue.size, 1, "Parser error! A const variable must have only one value")
    return possibleValue.first().text.trimIndent()
}

/**
 * Checks if PsiFile contains a constant property with the given element value.
 *
 * @param elementValue The value to search for in the constant properties.
 * @return true if a constant property with the specified value is found, false otherwise.
 */
fun PsiFile.hasConstantWithGivenValue(elementValue: String): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val elements = extractElementsOfTypes(KtProperty::class.java)
        elements.any { it.modifierList?.text?.contains("const") ?: false && it.getConstValue() == elementValue }
    }

/**
 * Retrieves the body with braces of a named function as a string.
 *
 * @return The body of the function as a string, or null if the function has no body.
 * @throws IllegalStateException if the function contains more than one body.
 */
private fun KtNamedFunction.getBlockBody(): String? {
    val possibleBody = extractElementsOfTypes(KtBlockExpression::class.java)
    if (possibleBody.isEmpty()) {
        return null
    }
    return possibleBody.first().text.trimBraces().trimIndent()
}

/**
 * Trims leading and trailing braces from a string.
 */
private fun String.trimBraces() = dropWhile { it.isWhitespace() }.removePrefix("{")
    .dropLastWhile { it.isWhitespace() }.removeSuffix("}")

/**
 * Finds methods within the given PsiFile that have the specified body content.
 *
 * @param content The body content to search for in the methods.
 * @return A list of method names whose bodies match the provided content.
 */
fun PsiFile.findMethodsWithContent(content: String): List<String> =
    WriteCommandAction.runWriteCommandAction<List<String>>(project) {
        formatting()
        val factory = PsiFileFactory.getInstance(project)
        val contentPsiFile = factory.createFileFromText("Content.kt", KotlinFileType.INSTANCE, content)
        val formattingContent = contentPsiFile.formatting() ?: ""

        val methods = ApplicationManager.getApplication().runReadAction<MutableCollection<KtNamedFunction>> {
            extractElementsOfTypes(KtNamedFunction::class.java)
        }
        methods.filter { it.getBlockBody() == formattingContent }.mapNotNull { it.name }.toList()
    }

/**
 * Finds all the methods in the file where a specific method is called.
 *
 * @param methodName The name of the method to search for its usages.
 * @return A list of strings containing the names of methods where the specified method is called.
 */
fun PsiFile.findMethodUsages(methodName: String): List<String> =
    ApplicationManager.getApplication().runReadAction<List<String>> {
        val callExpression = extractElementsOfTypes(KtCallExpression::class.java)
        callExpression.filter { it.text == methodName }.mapNotNull {
            it.parentsOfType(KtNamedFunction::class.java).firstOrNull()?.name
        }.toList()
    }

/**
 * Checks if the PsiFile contains an element of the specified type and with the given name.
 *
 * @param T the type of the PsiNamedElement subclass to check for
 * @param psiElementClass the class object representing the type of the element to check for
 * @param name the name of the element to search for
 * @return true if an element of the specified type and name exists in the PsiFile, false otherwise
 */
fun <T : PsiNamedElement> PsiFile.hasElementOfTypeWithName(psiElementClass: Class<out T>, name: String): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        extractElementsOfTypes(psiElementClass).any { it.name == name }
    }

/**
 * Checks if the PsiFile contains a property with the specified property name.
 *
 * @param propertyName The name of the property to search for.
 * @return True if the PsiFile contains a property with the given name, false otherwise.
 */
fun PsiFile.hasProperty(propertyName: String): Boolean = hasElementOfTypeWithName(KtProperty::class.java, propertyName)

/**
 * Checks if the PsiFile contains a method with the specified method name.
 *
 * @param methodName The name of the method to search for.
 * @return True if the PsiFile contains a method with the given name, false otherwise.
 */
fun PsiFile.hasMethod(methodName: String): Boolean = hasElementOfTypeWithName(KtNamedFunction::class.java, methodName)

/**
 * Checks whether the [PsiFile] contains a class with the specified [className].
 *
 * @param className The name of the class to check for.
 * @return `true` if the [PsiFile] contains a class with the specified [className], `false` otherwise.
 */
fun PsiFile.hasClass(className: String): Boolean = hasElementOfTypeWithName(KtClass::class.java, className)

/**
 * Checks if the [PsiFile] has a parameter with the specified name.
 *
 * @param parameterName the name of the parameter to check
 * @return true if the [PsiFile] contains a parameter with the specified name, false otherwise
 */
fun PsiFile.hasParameter(parameterName: String): Boolean = hasElementOfTypeWithName(KtParameter::class.java, parameterName)

/**
 * Retrieves the text of the parent element of the given PsiElement.
 *
 * @param element The PsiElement for which to find the parent.
 * @param isParentTypeFunction If true, the parent element is expected to be a function (KtNamedFunction).
 *                            If false, the parent element is expected to be a class or any other non-function type.
 * @return The text of the parent element if found, or "no name" if the parent is not found or has no name (for functions).
 */
private fun getParentText(element: PsiElement, isParentTypeFunction: Boolean): String? {
    return if (isParentTypeFunction) {
        element.parentsOfType(KtNamedFunction::class.java).firstOrNull()?.name
    } else {
        element.parent?.text
    }
}

/**
 * Checks if the PsiFile contains an expression with the specified text that has a specific parent element.
 *
 * @param expression The text of the expression to search for.
 * @param parent The text of the parent element to check against.
 * @param isParentTypeFunction If true, the parent element is expected to be a function (KtNamedFunction).
 *                            If false, the parent element is expected to be a class or any other non-function type.
 * @return True if the PsiFile contains an expression with the given text and the specified parent, false otherwise.
 */
fun PsiFile.hasExpressionWithParent(expression: String, parent: String?, isParentTypeFunction: Boolean): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val expressions: MutableCollection<PsiElement> =
            extractElementsOfTypes(KtDotQualifiedExpression::class.java, KtCallExpression::class.java)
        expressions.any { it.text == expression && getParentText(it, isParentTypeFunction) == parent }
    }
