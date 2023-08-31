package org.jetbrains.academy.test.system.java.ij.analyzer

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentsOfType
import org.jetbrains.academy.test.system.ij.formatting.formatting
import org.jetbrains.kotlin.psi.*

/** Extracts elements of given type from kotlin related files in project. */
/** Extracts elements of given type from [PsiElement] subtree. */
fun <T : PsiElement> PsiElement.extractElementsOfTypes(vararg psiElementClass: Class<out T>): MutableCollection<T> =
    psiElementClass.flatMap { PsiTreeUtil.collectElementsOfType(this, it) }.toMutableList()

/**
 * Retrieves the constant value of a property, if it is declared as a constant.
 *
 * @return The constant value of the property as a string, or null if the property is not constant or has no value.
 * @throws IllegalStateException if the constant property contains more than one value.
 */
private fun PsiField.getConstValue(): String? {
    val possibleValue = extractElementsOfTypes(PsiLiteralExpression::class.java)
    if (possibleValue.isEmpty()) {
        return null
    }
    require(possibleValue.size == 1) { "Parser error! A const variable must have only one value" }
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
        val elements = extractElementsOfTypes(PsiField::class.java)
        elements.any { it.modifierList?.text?.contains("final") ?: false && it.getConstValue() == elementValue }
    }

/**
 * Retrieves the body with braces of a named function as a string.
 *
 * @return The body of the function as a string, or null if the function has no body.
 * @throws IllegalStateException if the function contains more than one body.
 */
private fun PsiMethod.getBlockBody(): String? {
    val possibleBody = extractElementsOfTypes(PsiCodeBlock::class.java)
    if (possibleBody.isEmpty()) {
        return null
    }
    val a = possibleBody.first().text.trimBraces().trimIndent()
    return a
}

/**
 * Trims leading and trailing braces from a string.
 */
private fun String.trimBraces() = dropWhile { it.isWhitespace() }.removePrefix("{")
    .dropLastWhile { it.isWhitespace() }.removeSuffix("}")

/**
 * Formatting code content.
 *
 * @param content The source code to wrap and format.
 * @param project An instance of the project, used for creating PsiFile.
 * @return The formatted code content.
 */
private fun formattingContent(content: String, project: Project): String {
    val wrappedCode = "class WrappedClass { void wrappedMethod() { $content } }"
    val factory = PsiFileFactory.getInstance(project)
    val contentPsiFile = factory.createFileFromText("Content.java", JavaFileType.INSTANCE, wrappedCode)
    val formattingContent = contentPsiFile.formatting() ?: ""
    return formattingContent.lines().drop(2).dropLast(2).joinToString(System.lineSeparator()).trimIndent()
}

/**
 * Finds methods within the given PsiFile that have the specified body content.
 *
 * @param content The body content to search for in the methods.
 * @return A list of method names whose bodies match the provided content.
 */
fun PsiFile.findMethodsWithContent(content: String): List<String> =
    ApplicationManager.getApplication().runReadAction<List<String>> {
        formatting()
        val formattingContent = formattingContent(content, project)

        val methods = extractElementsOfTypes(PsiMethod::class.java)
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
        val callExpression = extractElementsOfTypes(PsiCallExpression::class.java)
        callExpression.filter { it.text == methodName }.mapNotNull {
            it.parentsOfType(PsiMethod::class.java).firstOrNull()?.name
        }.toList()
    }

/**
 * Checks if the PsiFile contains a filed with the specified name.
 *
 * @param filedName The name of the filed to search for.
 * @return True if the PsiFile contains a property with the given name, false otherwise.
 */
fun PsiFile.hasProperty(filedName: String): Boolean = ApplicationManager.getApplication().runReadAction<Boolean> {
    extractElementsOfTypes(PsiField::class.java).any { it.name == filedName }
}

/**
 * Checks if the PsiFile contains a method with the specified method name.
 *
 * @param methodName The name of the method to search for.
 * @return True if the PsiFile contains a method with the given name, false otherwise.
 */
fun PsiFile.hasMethod(methodName: String): Boolean = ApplicationManager.getApplication().runReadAction<Boolean> {
    extractElementsOfTypes(PsiMethod::class.java).any { it.name == methodName }
}

/**
 * Retrieves the text of the parent element of the given PsiElement.
 *
 * @param element The PsiElement for which to find the parent.
 * @param isParentTypeFunction If true, the parent element is expected to be a function.
 *                            If false, the parent element is expected to be a class or any other non-function type.
 * @return The text of the parent element if found, or "no name" if the parent is not found or has no name (for functions).
 */
private fun getParentText(element: PsiElement, isParentTypeFunction: Boolean): String? {
    return if (isParentTypeFunction) {
        element.parentsOfType(PsiMethod::class.java).firstOrNull()?.name
    } else {
        element.parent?.text
    }
}

/**
 * Checks if the PsiFile contains an expression with the specified text that has a specific parent element.
 *
 * @param expression The text of the expression to search for.
 * @param parent The text of the parent element to check against.
 * @param isParentTypeFunction If true, the parent element is expected to be a function.
 *                            If false, the parent element is expected to be a class or any other non-function type.
 * @return True if the PsiFile contains an expression with the given text and the specified parent, false otherwise.
 */
fun PsiFile.hasExpressionWithParent(expression: String, parent: String?, isParentTypeFunction: Boolean): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val expressions: MutableCollection<PsiElement> = extractElementsOfTypes(
                PsiNewExpression::class.java,
                PsiReferenceExpression::class.java,
                PsiMethodCallExpression::class.java
            )
        expressions.any { it.text == expression && getParentText(it, isParentTypeFunction) == parent }
    }
