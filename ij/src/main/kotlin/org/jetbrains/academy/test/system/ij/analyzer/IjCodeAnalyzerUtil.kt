package org.jetbrains.academy.test.system.ij.analyzer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.academy.test.system.ij.formatting.formatting
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.*

/** Extracts [kotlin elements][KtElement] of given type from kotlin related files in project. */
/** Extracts elements of given type from [PsiElement] subtree. */
fun <T : PsiElement> PsiElement.extractElementsOfTypes(psiElementClassList: List<Class<out T>>): MutableCollection<T> =
    psiElementClassList.flatMap { PsiTreeUtil.collectElementsOfType(this, it) }.toMutableList()

/**
 * Retrieves the constant value of a property, if it is declared as a constant.
 *
 * @return The constant value of the property as a string, or null if the property is not constant or has no value.
 * @throws IllegalStateException if the constant property contains more than one value.
 */
private fun KtProperty.getConstValue(): String? {
    val possibleValue =
        extractElementsOfTypes(listOf(KtConstantExpression::class.java, KtStringTemplateExpression::class.java))
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
        val elements = extractElementsOfTypes(listOf(KtProperty::class.java))
        elements.any { it.modifierList?.text?.contains("const") ?: false && it.getConstValue() == elementValue }
    }

/**
 * Retrieves the body of a named function as a string.
 *
 * @return The body of the function as a string, or null if the function has no body.
 * @throws IllegalStateException if the function contains more than one body.
 */
private fun KtNamedFunction.getBody(): String? {
    val possibleBody = extractElementsOfTypes(listOf(KtBlockExpression::class.java))
    if (possibleBody.isEmpty()) {
        return null
    }
    require(possibleBody.size == 1) { "Parser error! A function must have only one body" }
    return possibleBody.first().text
        .split(System.lineSeparator()).drop(1).dropLast(1).joinToString(System.lineSeparator()).trimIndent()
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
        val factory = PsiFileFactory.getInstance(project)
        val contentPsiFile = factory.createFileFromText("Content.kt", KotlinFileType.INSTANCE, content)
        val formattingContent = contentPsiFile.formatting() ?: ""

        val methods = extractElementsOfTypes(listOf(KtNamedFunction::class.java))
        methods.filter { it.getBody() == formattingContent }.mapNotNull { it.name }.toList()
    }
