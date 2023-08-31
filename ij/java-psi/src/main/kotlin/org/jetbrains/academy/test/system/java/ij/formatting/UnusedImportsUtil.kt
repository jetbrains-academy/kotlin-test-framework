package org.jetbrains.academy.test.system.java.ij.formatting

import com.intellij.psi.PsiFile
import com.intellij.codeInspection.unusedImport.UnusedImportInspection
import org.jetbrains.academy.test.system.inspections.applyGlobalInspections

fun PsiFile.checkIfOptimizeImportsWereApplied() {
    assert(applyGlobalInspections(listOf(UnusedImportInspection()))) {
        "Please, apply \"Optimize import\" option when formatting code."
    }
}
