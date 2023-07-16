package org.jetbrains.academy.test.system.ij.formatting

class UnusedImportsTests : BaseFormattingUtilTests() {
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
