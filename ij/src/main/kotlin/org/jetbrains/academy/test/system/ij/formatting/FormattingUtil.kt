@file:Suppress("ForbiddenComment")

package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
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

fun PsiFile.checkIfInspectionsWerePassed(inspections: List<LocalInspectionTool>): Boolean {
    val problems: MutableList<ProblemDescriptor> = mutableListOf()
    val inspectionManager = InspectionManager.getInstance(project)
    ProgressManager.getInstance().executeProcessUnderProgress(
        {
            for (inspection in inspections) {
                problems.addAll(
                    ApplicationManager.getApplication().runReadAction<List<ProblemDescriptor>> {
                        inspection.processFile(this, inspectionManager)
                    })
            }
        },
        DaemonProgressIndicator()
    )
    return problems.isEmpty()
}

fun PsiFile.checkIfOptimizeImportsWereApplied() {
    assert(this.checkIfInspectionsWerePassed(listOf(KotlinUnusedImportInspection()))) {
        "Please, apply \"Optimize import\" option when formatting code."
    }
}
