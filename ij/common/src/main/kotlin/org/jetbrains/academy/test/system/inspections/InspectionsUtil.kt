@file:Suppress("ForbiddenComment")

package org.jetbrains.academy.test.system.inspections

import com.intellij.codeInsight.daemon.impl.DaemonProgressIndicator
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.GlobalSimpleInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.ProblemDescriptionsProcessor
import com.intellij.codeInspection.CommonProblemDescriptor
import com.intellij.codeInspection.ex.GlobalInspectionContextBase
import com.intellij.codeInspection.reference.RefEntity
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

fun PsiFile.applyGlobalInspections(inspections: List<GlobalSimpleInspectionTool>): Boolean {
    // TODO: `checkFile` doesn't work due to `resolve` doesn't work
    val inspectionManager = InspectionManager.getInstance(project)
    val problemDescriptionsProcessor = MyProblemDescriptionsProcessor()
    val globalContext = GlobalInspectionContextBase(project)
    val problemsHolder = ProblemsHolder(inspectionManager, this, true)
    ProgressManager.getInstance().executeProcessUnderProgress(
        {
            for (inspection in inspections) {
                ApplicationManager.getApplication().runReadAction<Unit> {
                    inspection.checkFile(this, inspectionManager, problemsHolder, globalContext, problemDescriptionsProcessor)
                }
            }
        },
        DaemonProgressIndicator()
    )
    return !problemsHolder.hasResults()
}

class MyProblemDescriptionsProcessor: ProblemDescriptionsProcessor {

    private val problems: MutableList<CommonProblemDescriptor?> = mutableListOf()

    override fun addProblemElement(refEntity: RefEntity?, vararg commonProblemDescriptors: CommonProblemDescriptor?) {
        problems.addAll(commonProblemDescriptors)
    }

    fun getProblems(): MutableList<CommonProblemDescriptor?> {
        return problems
    }
}
