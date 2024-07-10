package org.jetbrains.academy.test.system.java.ij.analyzer

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import org.jetbrains.academy.test.system.ij.analyzer.extractElementsOfTypes
import org.jetbrains.academy.test.system.ij.analyzer.getBlockBody
import org.jetbrains.academy.test.system.ij.analyzer.getConstValue
import org.jetbrains.academy.test.system.ij.formatting.formatting

/**
 * Checks if PsiFile contains a constant property with the given element value.
 *
 * @param elementValue The value to search for in the constant properties.
 * @return true if a constant property with the specified value is found, false otherwise.
 */
fun PsiFile.hasConstantWithGivenValue(elementValue: String): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val elements = extractElementsOfTypes(PsiField::class.java)
        elements.any { it.modifierList?.text?.contains("final") ?: false && it.getConstValue(PsiLiteralExpression::class.java) == elementValue }
    }

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
    WriteCommandAction.runWriteCommandAction<List<String>>(project) {
        formatting()
        val formattingContent = formattingContent(content, project)

        val methods = extractElementsOfTypes(PsiMethod::class.java)
        methods.filter { it.getBlockBody(PsiCodeBlock::class.java) == formattingContent }.mapNotNull { it.name }.toList()
    }

/**
 * Retrieves the arguments of a method call with the given method name.
 *
 * @param methodName The name of the method to retrieve the arguments for.
 * @return A list of strings representing the arguments of the method call,
 *         or null if no method call with the given name is found.
 */
fun PsiFile.getMethodCallArguments(methodName: String): List<String>? =
    ApplicationManager.getApplication().runReadAction<List<String>?> {
        extractElementsOfTypes(PsiMethodCallExpression::class.java)
            .firstOrNull { it.methodExpression.referenceName == methodName }
            ?.argumentList?.expressions?.mapNotNull { it.text }
    }
