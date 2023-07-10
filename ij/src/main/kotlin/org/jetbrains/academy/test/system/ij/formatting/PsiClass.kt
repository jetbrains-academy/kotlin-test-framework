package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PsiClass(private val sourceText: String) : BasePlatformTestCase() {

    fun getPsiFile(): PsiFile? {
        setUp()
        myFixture.configureByText("Task.kt", sourceText)
        return myFixture.file
    }
}
