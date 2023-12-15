@file:Suppress("ForbiddenComment")

package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.junit.jupiter.api.Assertions

// TODO: make it possible to check different aspects of formatting
fun PsiFile.checkIfFormattingRulesWereApplied() {
    val originalCode = ApplicationManager.getApplication().runReadAction<String> { text }
    val codeStyleManager = CodeStyleManager.getInstance(project)
    WriteCommandAction.runWriteCommandAction(project) {
        codeStyleManager.reformat(this)
    }
    val formattedCode = ApplicationManager.getApplication().runReadAction<String> { text }
    Assertions.assertEquals(
        originalCode.trimIndent(),
        formattedCode.trimIndent(),
        "The code after formatting should be:${System.lineSeparator()}$formattedCode${System.lineSeparator()}Please, apply code formatting refactoring to the code."
    )
}

fun PsiFile.formatting(): String? {
    val codeStyleManager = CodeStyleManager.getInstance(project)
    WriteCommandAction.runWriteCommandAction(project) {
        codeStyleManager.reformat(this)
    }
    return ApplicationManager.getApplication().runReadAction<String> { text }
}
