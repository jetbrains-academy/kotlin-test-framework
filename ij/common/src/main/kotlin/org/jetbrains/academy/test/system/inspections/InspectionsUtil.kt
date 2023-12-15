package org.jetbrains.academy.test.system.inspections

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiFile

fun PsiFile.applyLocalInspections(inspections: List<LocalInspectionTool>): List<ProblemDescriptor> {
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
    return problems
}
