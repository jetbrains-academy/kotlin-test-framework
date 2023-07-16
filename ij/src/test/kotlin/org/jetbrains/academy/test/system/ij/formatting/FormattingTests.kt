package org.jetbrains.academy.test.system.ij.formatting

class FormattingTests : BaseFormattingUtilTests() {
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
}
