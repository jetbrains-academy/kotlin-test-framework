package org.jetbrains.academy.test.system.java.ij.formatting

import com.intellij.psi.PsiFile
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.JavaCodeStyleManager

fun PsiFile.checkIfOptimizeImportsWereApplied() {
    val originalCode = ApplicationManager.getApplication().runReadAction<String> { text }
    WriteCommandAction.runWriteCommandAction(project) {
        JavaCodeStyleManager.getInstance(project).optimizeImports(this)
    }
    val optimizedCode = ApplicationManager.getApplication().runReadAction<String> { text }
    assert(originalCode.trimIndent() == optimizedCode.trimIndent()) {
        "Please, apply \"Optimize import\" option when formatting code."
    }
}
