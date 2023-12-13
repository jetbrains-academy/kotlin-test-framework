package org.jetbrains.academy.test.system.kotlin.ij.analyzer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import org.jetbrains.academy.test.system.ij.analyzer.*
import org.jetbrains.academy.test.system.ij.formatting.formatting
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtBlockExpression

/**
 * Checks if PsiFile contains a constant property with the given element value.
 *
 * @param elementValue The value to search for in the constant properties.
 * @return true if a constant property with the specified value is found, false otherwise.
 */
fun PsiFile.hasConstantWithGivenValue(elementValue: String): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val elements = extractElementsOfTypes(KtProperty::class.java)
        elements.any { it.modifierList?.text?.contains("const") ?: false && it.getConstValue(KtConstantExpression::class.java, KtStringTemplateExpression::class.java) == elementValue }
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
        val factory = PsiFileFactory.getInstance(project)
        val contentPsiFile = factory.createFileFromText("Content.kt", KotlinFileType.INSTANCE, content)
        val formattingContent = contentPsiFile.formatting() ?: ""

        val methods = ApplicationManager.getApplication().runReadAction<MutableCollection<KtNamedFunction>> {
            extractElementsOfTypes(KtNamedFunction::class.java)
        }
        methods.filter { it.getBlockBody(KtBlockExpression::class.java) == formattingContent }.mapNotNull { it.name }.toList()
    }
