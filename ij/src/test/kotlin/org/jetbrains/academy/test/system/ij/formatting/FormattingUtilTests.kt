package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FormattingUtilTests : BasePlatformTestCase() {

    private fun testWithError(code: String, action: (PsiFile) -> Unit) {
        assertThrows(AssertionError::class.java) { action(myFixture.configureByText("dummy.kt", code)) }
    }

    private fun testWithoutError(code: String, action: (PsiFile) -> Unit) {
        action(myFixture.configureByText("dummy.kt", code))
    }

    fun testWrongFormatting() {
        testWithError(
            """
                fun funWithFormattingIssues() {
                      println ("This function definitely has formatting issues" )
                    println( "... that could be easily fixed using one shortcut")
                    for ( i in 1 ..10) {
                  println("Please, format me!")
                    }
                }
            """.trimIndent()
        ) { it.checkIfFormattingRulesWereApplied() }
    }

    fun testRightFormatting() {
        testWithoutError(
            """
                fun funWithFormattingIssues() {
                    println("This function definitely has formatting issues")
                    println("... that could be easily fixed using one shortcut")
                    for (i in 1..10) {
                        println("Please, format me!")
                    }
                }
            """.trimIndent()
        ) { it.checkIfFormattingRulesWereApplied() }
    }

    fun testUnusedImports() {
        testWithError(
            """
                import java.io.File
                
                fun funWithFormattingIssues() {
                    println("This function definitely has formatting issues")
                    println("... that could be easily fixed using one shortcut")
                    for (i in 1..10) {
                        println("Please, format me!")
                    }
                }
            """.trimIndent()
        )
        { it.checkIfOptimizeImportsWereApplied() }
    }

    fun testUsedImports() {
        testWithoutError(
            """
                fun funWithFormattingIssues() {
                    println("This function definitely has formatting issues")
                    println("... that could be easily fixed using one shortcut")
                    for (i in 1..10) {
                        println("Please, format me!")
                    }
                }
            """.trimIndent()
        ) { it.checkIfOptimizeImportsWereApplied() }
    }
}
