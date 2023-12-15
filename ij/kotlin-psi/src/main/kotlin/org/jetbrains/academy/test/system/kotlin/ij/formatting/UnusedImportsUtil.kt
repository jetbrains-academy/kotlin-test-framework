package org.jetbrains.academy.test.system.kotlin.ij.formatting

import com.intellij.psi.PsiFile
import org.jetbrains.academy.test.system.inspections.applyLocalInspections
import org.jetbrains.kotlin.idea.inspections.KotlinUnusedImportInspection

fun PsiFile.checkIfOptimizeImportsWereApplied() {
    assert(applyLocalInspections(listOf(KotlinUnusedImportInspection())).isEmpty()) {
        "Please, apply \"Optimize import\" option when formatting code."
    }
}
