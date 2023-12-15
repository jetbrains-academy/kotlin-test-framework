package org.jetbrains.academy.test.system.java.ij

import org.jetbrains.academy.test.system.java.ij.formatting.checkIfOptimizeImportsWereApplied

class UnusedImportsTests : BaseFormattingUtilTests() {

    fun testUnusedImports() {
        testWithError(
            """
                import java.io.File;
                import com.intellij.psi.PsiElement;

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
        )
        { it.checkIfOptimizeImportsWereApplied() }
    }

    fun testUsedImports() {
        testWithoutError(
            """
                import java.io.File;
                
                public class ExampleClass {
                
                    public static void funWithFormattingIssues() {
                        System.out.println("This function definitely has formatting issues");
                        System.out.println("... that could be easily fixed using one shortcut");
                        for (int i = 1; i <= 10; i++) {
                            System.out.println("Please, format me!");
                        }
                        Class<File> file = File.class;
                        System.out.println(file);
                    }
                }
            """.trimIndent()
        ) { it.checkIfOptimizeImportsWereApplied() }
    }
}
