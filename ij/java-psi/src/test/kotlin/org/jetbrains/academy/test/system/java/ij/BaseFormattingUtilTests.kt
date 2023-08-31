package org.jetbrains.academy.test.system.java.ij

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore

@Ignore
open class BaseFormattingUtilTests : BasePlatformTestCase() {

    protected fun testWithError(code: String, action: (PsiFile) -> Unit) {
        assertThrows(AssertionError::class.java) { action(myFixture.configureByText("dummy.java", code)) }
    }

    protected fun testWithoutError(code: String, action: (PsiFile) -> Unit) {
        action(myFixture.configureByText("dummy.java", code))
    }
}
