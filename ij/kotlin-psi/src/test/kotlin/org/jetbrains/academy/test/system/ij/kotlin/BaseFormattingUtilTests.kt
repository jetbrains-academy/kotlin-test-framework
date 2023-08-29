package org.jetbrains.academy.test.system.ij.kotlin

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore

@Ignore
open class BaseFormattingUtilTests : BasePlatformTestCase() {

    protected fun testWithError(code: String, action: (PsiFile) -> Unit) {
        assertThrows(AssertionError::class.java) { action(myFixture.configureByText("dummy.kt", code)) }
    }

    protected fun testWithoutError(code: String, action: (PsiFile) -> Unit) {
        action(myFixture.configureByText("dummy.kt", code))
    }
}
