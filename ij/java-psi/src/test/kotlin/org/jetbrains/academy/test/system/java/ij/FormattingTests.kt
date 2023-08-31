package org.jetbrains.academy.test.system.java.ij

import org.jetbrains.academy.test.system.ij.formatting.checkIfFormattingRulesWereApplied

class FormattingTests : BaseFormattingUtilTests() {
    fun testWrongFormatting() {
        testWithError(
            """
                public class ExampleClass {
                
                    public static void funWithFormattingIssues() {
                          System.out.println ("This function definitely has formatting issues" );
                        System.out.println(  "... that could be easily fixed using one shortcut");
                        for ( int i = 1; i <= 10; i++) {
                      System.out.println("Please, format me!");
                        }
                    }
                }
            """.trimIndent()
        ) { it.checkIfFormattingRulesWereApplied() }
    }

    fun testRightFormatting() {
        testWithoutError(
            """
                public class ExampleClass {

                    public static void funWithFormattingIssues() {
                        System.out.println("This function definitely has formatting issues");
                        System.out.println("... that could be easily fixed using one shortcut");
                        for (int i = 1; i <= 10; i++) {
                            System.out.println("Please, format me!");
                        }
                    }
                }
            """.trimIndent()
        ) { it.checkIfFormattingRulesWereApplied() }
    }
}
