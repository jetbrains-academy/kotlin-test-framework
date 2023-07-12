@file:Suppress("ForbiddenComment")

package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.idea.inspections.KotlinUnusedImportInspection

// TODO: make it possible to check different aspects of formatting
fun PsiFile.checkIfFormattingRulesWereApplied() {
    val originalCode = ApplicationManager.getApplication().runReadAction<String> { text }
    val codeStyleManager = CodeStyleManager.getInstance(project)
    WriteCommandAction.runWriteCommandAction(project) {
        codeStyleManager.reformat(this)
    }
    val formattedCode = ApplicationManager.getApplication().runReadAction<String> { text }
    assert(originalCode.trimIndent() == formattedCode.trimIndent()) { "The code after formatting should be:${System.lineSeparator()}$formattedCode${System.lineSeparator()}Please, apply code formatting refactoring to the code." }
}

fun PsiFile.checkIfOptimizeImportsWereApplied() {
    val inspection = KotlinUnusedImportInspection()
    val problems: MutableList<ProblemDescriptor> = mutableListOf()
    val inspectionManager = InspectionManager.getInstance(project)
    ProgressManager.getInstance().executeProcessUnderProgress(
        {
            problems.addAll(
                ApplicationManager.getApplication().runReadAction<List<ProblemDescriptor>> {
                inspection.processFile(this, inspectionManager)
                })
        },
        DaemonProgressIndicator()
    )
    assert(problems.isNotEmpty()) { "Please, apply \"Optimize import\" option when formatting code." }
}
