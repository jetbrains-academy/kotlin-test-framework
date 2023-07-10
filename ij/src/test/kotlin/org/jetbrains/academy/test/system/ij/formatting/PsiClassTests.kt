package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PsiClassTests : BasePlatformTestCase() {

    fun testPsiFile() {
        val sourceText =
            """
                fun funWithFormattingIssues() {
                    println ("This function is definitely has formatting issues" )
                    println( "... that could be easily fixed using one shortcut")
                    for ( i in 1 ..10) {
                        println("Please, format me!")
                    }
                }
            """.trimIndent()
        val psi = myFixture.configureByText("Test.kt", sourceText)
        val psiClass = PsiClass(sourceText)
        val psiFile = psiClass.getPsiFile()
        assert(psi.context == psiFile?.context) { "Psi File is not correctly" }
    }
}
