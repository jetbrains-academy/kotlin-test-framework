package org.jetbrains.academy.test.system.java.ij

import org.jetbrains.academy.test.system.java.test.BaseIjTestClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.Arguments

class BaseIjTestClassTests : BaseIjTestClass() {

    companion object {
        @JvmStatic
        fun findMethodUsagesTestProvider() = listOf(
            Arguments.of("method(\"Content\")", listOf("outerFunction", "method2")),
            Arguments.of("method(\"y is greater than 5\")", listOf("method1")),
            Arguments.of("method(\"y is less than 5\")", listOf("method1")),
            Arguments.of("method(content)", emptyList<String>())
        )

        @JvmStatic
        fun hasExpressionWithParentTestProvider() = listOf(
            Arguments.of("productPrice.size()", "sum / productPrice.size()"),
            Arguments.of("new File(\"Exception.txt\")", "(new File(\"Exception.txt\"), \"UTF-8\")"),
            Arguments.of("new PrintWriter(new File(\"Exception.txt\"), \"UTF-8\")", "calculateAveragePrice"),
            Arguments.of("Integer.MAX_VALUE", "private static final int CONSTANT = Integer.MAX_VALUE;")
        )

        @JvmStatic
        fun findMethodsWithContentTestProvider() = listOf(
            Arguments.of("""
            String actions = "Some actions";
            System.out.println("Content");
            System.out.println(actions);
        """, "method1")
        )
    }

    @ParameterizedTest
    @MethodSource("findMethodsWithContentTestProvider")
    fun testFindMethodsWithContent(content: String, methodName: String) {
        val example = """
            public class ExampleClass {

                public void method1() {
                    String actions = "Some actions";
                    System.out.println("Content");
                    System.out.println(actions);
                }
            
                public void method2() {
                    String content = "Content";
                    String actions = "Some actions";
                    System.out.println(actions + content);
                }
            
                public void method3() {
                    System.out.println("Content");
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
    }

    @ParameterizedTest
    @MethodSource("findMethodsWithContentTestProvider")
    fun testFindMethodsWithContentWithBrokenFormatting(content: String, methodName: String) {
        val example = """
            public class ExampleClass {

                public void method1() {
                          String actions = "Some actions";
                       System.out.println("Content");
                    System.out.println(actions);
                }
           
            
                  public void method2() {
                      System.out.println("Content");
                  }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
    }

    fun testFindMethodsWithContentWithNestedBodies() {
        val example = """
            public class ExampleClass {
            
                public static int outerFunction(int x) {
                    System.out.println("Outer function started.");
            
                    Operation operation = new Operation() {
                        @Override
                        public int innerFunction(int y) {
                            return y * y;
                        }
                    };
            
                    int squaredX = operation.calculate(x);
                    int modifiedResult = squaredX + 10;
                    return modifiedResult;
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        val content = "return y * y;"
        val methodName = "innerFunction"
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
    }

    fun testHasConstantWithGivenValue() {
        val example = """
            public class ExampleClass {
            
                private static final String CONSTANT1 = "some text";
                private double notConstant = 0.5;
                public static final int CONSTANT2 = 2;
                public final int CONSTANT50 = 50;
                public int consts = 500;
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        var value = "\"some text\""
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        value = "2"
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        value = "50"
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        assertFalse(hasConstantWithGivenValue("0.5"))
        assertFalse(hasConstantWithGivenValue("500"))
    }

    fun testHasConstantWithGivenValueWithBrokenFormatting() {
        val example = """
            public class ExampleClass {
            
              private static final String CONSTANT1 = "some text";
                   private double notConstant = 0.5;
               public static final int CONSTANT2 = 2;
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        var value = "\"some text\""
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        value = "2"
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        assertFalse(hasConstantWithGivenValue("0.5"))
    }

    @ParameterizedTest
    @MethodSource("findMethodUsagesTestProvider")
    fun testFindMethodUsages(methodName: String, methodsList: List<String>) {
        val example = """
            public class ExampleClass {
            
                public static int outerFunction(int x) {
                    Function<Integer, Integer> innerFunction = y -> y * y;
                    method("Content");
                    int squaredX = innerFunction.apply(x);
                    return squaredX + 10;
                }

                public void method(String message) {
                    System.out.println(message);
                }

                public void method1(int y) {
                    String actions = "Some actions";
                    if (y > 5) {
                        method("y is greater than 5");
                    } else {
                        method("y is less than 5");
                    }
                }

                public void method2() {
                    method("Content");
                    System.out.println("Content");
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        assert(methodsList.equals(findMethodUsages(methodName))) {
            "Method $methodName should be called in methods: $methodsList"
        }
    }

    fun testHasProperty() {
        val example = """
            public class ExampleClass {
                private static final String CONSTANT = "some text";
                private final double value = 0.5;
                private int number = 2;

                public void method() {
                    System.out.println("Content");
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        var value = "CONSTANT"
        assert(hasProperty(value)) { "There must exist a property with name $value" }
        value = "value"
        assert(hasProperty(value)) { "There must exist a property with name $value" }
        value = "number"
        assert(hasProperty(value)) { "There must exist a property with name $value" }
        assertFalse(hasProperty("method"))
        assertFalse(hasProperty("Content"))
    }

    fun testHasMethod() {
        val example = """
            public class ExampleClass {
                private static final String CONSTANT = "some text";
                private static final double value = 0.5;

                public void method() {
                    String actions = "Some actions";
                    System.out.println("Content");
                    System.out.println(actions);
                }

                public void notMethod() {
                    String content = "Content";
                    String actions = "Some actions";
                    System.out.println(actions + content);
                }

                public void value() {
                    System.out.println("Content");
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        var value = "method"
        assert(hasMethod(value)) { "There must exist a method with name $value" }
        value = "notMethod"
        assert(hasMethod(value)) { "There must exist a method with name $value" }
        value = "value"
        assert(hasMethod(value)) { "There must exist a method with name $value" }
        assertFalse(hasMethod("CONSTANT"))
        assertFalse(hasMethod("Content"))
    }

    fun testHasClass() {
        val example = """
            public class ExampleClass {
                public void method() {
                    String actions = "Some actions";
                    System.out.println(actions);
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        val name = "ExampleClass"
        Assertions.assertTrue(hasClass(name), "There must exist a class with name $name")
        Assertions.assertFalse(hasClass("method"))
        Assertions.assertFalse(hasClass("Class"))
    }

    fun testHasParameter() {
        val example = """
            public class ExampleClass {
                private static final String PARAMETER = "some text";

                public void method1(Int parameter1, String parameter2) {
                    System.out.println(parameter2);
                }

                public void method2(Double y) {
                    System.out.println("Content");
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        var value = "parameter1"
        Assertions.assertTrue(hasParameter(value), "There must exist a parameter with name $value")
        value = "parameter2"
        Assertions.assertTrue(hasParameter(value), "There must exist a parameter with name $value")
        value = "y"
        Assertions.assertTrue(hasParameter(value), "There must exist a parameter with name $value")
        Assertions.assertFalse(hasParameter("PARAMETER"))
        Assertions.assertFalse(hasParameter("Content"))
    }

    @ParameterizedTest
    @MethodSource("hasExpressionWithParentTestProvider")
    fun testHasExpressionWithParent(expression: String, parent: String?) {
        val example = """
            import java.io.File;
            import java.io.PrintWriter;
            import java.util.List;
            
            public class ExampleClass {
            
                private static final int CONSTANT = Integer.MAX_VALUE;
            
                public static Integer calculateAveragePrice(List<Integer> productPrice) {
                    try {
                        int sum = 0;
                        for (Integer price : productPrice) {
                            sum += price;
                        }
                        return sum / productPrice.size();
                    } catch (Exception error) {
                        try (PrintWriter writer = new PrintWriter(new File("Exception.txt"), "UTF-8")) {
                            writer.print(error.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.java", example)
        assert(hasExpressionWithParent(expression, parent)) {
            "There must exist an expression $expression with parent $parent"
        }
    }
}
