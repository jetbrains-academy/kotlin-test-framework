package org.jetbrains.academy.test.system.ij.formatting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FormattingUtilTests : BasePlatformTestCase() {

    fun testWrongFormatting() {
        val psi = myFixture.configureByText(
            "dummy.kt",
            """
                fun funWithFormattingIssues() {
                      println ("This function is definitely has formatting issues" )
                    println( "... that could be easily fixed using one shortcut")
                    for ( i in 1 ..10) {
                  println("Please, format me!")
                    }
                }
            """.trimIndent()
        )
        assertThrows(AssertionError::class.java) {
            psi.checkIfFormattingRulesWereApplied()
        }
    }

    fun testRightFormatting() {
        val psi = myFixture.configureByText(
            "dummy.kt",
            """
                fun funWithFormattingIssues() {
                    println("This function is definitely has formatting issues")
                    println("... that could be easily fixed using one shortcut")
                    for (i in 1..10) {
                        println("Please, format me!")
                    }
                }
            """.trimIndent()
        )
        psi.checkIfFormattingRulesWereApplied()
    }
}
