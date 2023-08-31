package org.jetbrains.academy.test.system.java.ij

import org.jetbrains.academy.test.system.java.test.BaseIjTestClass

class BaseIjTestClassTests : BaseIjTestClass() {

    fun testFindMethodsWithContent() {
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
        val content = """
            String actions = "Some actions";
            System.out.println("Content");
            System.out.println(actions);
        """.trimIndent()
        val methodName = "method1"
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
    }

    fun testFindMethodsWithContentWithBrokenFormatting() {
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
        val content = """
            String actions = "Some actions";
            System.out.println("Content");
            System.out.println(actions);
        """.trimIndent()
        val methodName = "method1"
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

    fun testFindMethodUsages() {
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
        var methodName = "method(\"Content\")"
        var methodsList = listOf("outerFunction", "method2")
        assert(methodsList.equals(findMethodUsages(methodName))) {
            "Method $methodName should be called in methods: $methodsList"
        }
        methodName = "method(\"y is greater than 5\")"
        methodsList = listOf("method1")
        assert(methodsList.equals(findMethodUsages(methodName))) {
            "Method $methodName should be called in methods: $methodsList"
        }
        methodName = "method(\"y is less than 5\")"
        methodsList = listOf("method1")
        assert(methodsList.equals(findMethodUsages(methodName))) {
            "Method $methodName should be called in methods: $methodsList"
        }
        methodName = "method(content)"
        methodsList = listOf()
        assert(methodsList.equals(findMethodUsages(methodName))) {
            "Method $methodName should not be called"
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

    fun testHasExpressionWithParent() {
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
        var expression = "productPrice.size()"
        var parent: String? = "sum / productPrice.size()"
        assert(hasExpressionWithParent(expression, parent)) {
            "There must exist an expression $expression with parent $parent"
        }
        expression = "new File(\"Exception.txt\")"
        parent = "(new File(\"Exception.txt\"), \"UTF-8\")"
        assert(hasExpressionWithParent(expression, parent)) {
            "There must exist an expression $expression with parent $parent"
        }
        expression = "new PrintWriter(new File(\"Exception.txt\"), \"UTF-8\")"
        parent = "calculateAveragePrice"
        assert(hasExpressionWithParent(expression, parent, true)) {
            "There must exist an expression $expression with parent $parent"
        }
        expression = "Integer.MAX_VALUE"
        parent = "private static final int CONSTANT = Integer.MAX_VALUE;"
        assert(hasExpressionWithParent(expression, parent)) {
            "There must exist an expression $expression with parent $parent"
        }
    }
}
