package org.jetbrains.academy.test.system.kotlin.ij

import org.junit.jupiter.api.Assertions
import org.jetbrains.academy.test.system.kotlin.test.BaseIjTestClass
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BaseIjTestClassTests : BaseIjTestClass() {

    companion object {
        @JvmStatic
        fun findMethodUsagesTestProvider() = listOf(
            Arguments.of("method(\"Content\")", listOf("innerFunction", "method2")),
            Arguments.of("method(\"y is greater than 5\")", listOf("method1")),
            Arguments.of("method(\"y is less than 5\")", listOf("method1")),
            Arguments.of("method(content)", emptyList<String>())
        )

        @JvmStatic
        fun hasExpressionWithParentTestProvider() = listOf(
            Arguments.of("productPrice.sum()", "productPrice.sum() / productPrice.count()"),
            Arguments.of("File(\\\"Exception.txt\\\")", "File(\\\"Exception.txt\\\")"),
            Arguments.of("PrintWriter(File(\\\"Exception.txt\\\"), Charsets.UTF_8).use { it.print(error.toString()) }", "calculateAveragePrice"),
            Arguments.of("Int.MAX_VALUE", "val CONSTANT = Int.MAX_VALUE"),
            Arguments.of("Int.MAX_VALUE", null)
        )

        @JvmStatic
        fun findMethodsWithContentTestProvider() = listOf(
            Arguments.of("""
            val actions = "Some actions"
            println("Content")
            println(actions)
        """, "method1")
        )
    }

    @ParameterizedTest
    @MethodSource("findMethodsWithContentTestProvider")
    fun testFindMethodsWithContent(content: String, methodName: String) {
        val example = """
            class ExampleClass {

                fun method1() {
                    val actions = "Some actions"
                    println("Content")
                    println(actions)
                }
                
                fun method2() {
                    val content = "Content"
                    val actions = "Some actions"
                    println(actions + content)
                }
                
                fun method3() {
                    println("Content")
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        Assertions.assertEquals(
            listOf(methodName),
            findMethodsWithContent(content),
            "The name of a method with this content \n $content \n must be $methodName"
        )
    }

    fun testFindMethodsWithSingleLineContent() {
        val example = """
            class ExampleClass {
                fun method() =  { "Some actions" }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        val content = """"Some actions"""".trimIndent()
        val methodName = "method"
        Assertions.assertEquals(
            listOf(methodName),
            findMethodsWithContent(content),
            "The name of a method with this content \n $content \n must be $methodName"
        )
    }

    @ParameterizedTest
    @MethodSource("findMethodsWithContentTestProvider")
    fun testFindMethodsWithContentWithBrokenFormatting(content: String, methodName: String) {
        val example = """
            class ExampleClass {

              fun method1() {
                        val actions = "Some actions"
                    println("Content")
                  println(actions)
              }
                
                fun method2() {
                    println("Content")
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        Assertions.assertEquals(
            listOf(methodName),
            findMethodsWithContent(content),
            "The name of a method with this content \n $content \n must be $methodName"
        )
    }

    fun testFindMethodsWithContentWithNestedBodies() {
        val example = """
            fun outerFunction(x: Int): Int {
                fun innerFunction(y: Int): Int {
                    return y * y
                }
        
                val squaredX = innerFunction(x)
                return squaredX + 10
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        val content = "return y * y"
        val methodName = "innerFunction"
        Assertions.assertEquals(
            listOf(methodName),
            findMethodsWithContent(content),
            "The name of a method with this content \n $content \n must be $methodName"
        )
    }

    fun testHasConstantWithGivenValue() {
        val example = """
            private const val CONSTANT1 = "some text"
            private val notConstant = 0.5
            const val CONSTANT2 = 2
            const val CONSTANT50 = 50
            val const = 500
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        var value = "\"some text\""
        Assertions.assertTrue(hasConstantWithGivenValue(value), "There must exist a constant with value $value")
        value = "2"
        Assertions.assertTrue(hasConstantWithGivenValue(value), "There must exist a constant with value $value")
        value = "50"
        Assertions.assertTrue(hasConstantWithGivenValue(value), "There must exist a constant with value $value")
        Assertions.assertFalse(hasConstantWithGivenValue("0.5"))
        Assertions.assertFalse(hasConstantWithGivenValue("500"))
    }

    fun testHasConstantWithGivenValueWithBrokenFormatting() {
        val example = """
          private const val CONSTANT1 = "some text"
             private val notConstant = 0.5
           const val CONSTANT2 = 2
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        var value = "\"some text\""
        Assertions.assertTrue(hasConstantWithGivenValue(value), "There must exist a constant with value $value")
        value = "2"
        Assertions.assertTrue(hasConstantWithGivenValue(value), "There must exist a constant with value $value")
        Assertions.assertFalse(hasConstantWithGivenValue("0.5"))
    }

    @ParameterizedTest
    @MethodSource("findMethodUsagesTestProvider")
    fun testFindMethodUsages(methodName: String, methodsList: List<String>) {
        val example = """
            class ExampleClass {
                fun outerFunction(x: Int): Int {
                    fun innerFunction(y: Int): Int {
                        return y * y
                        method("Content")
                    }
            
                    val squaredX = innerFunction(x)
                    return squaredX + 10
                }
                
                fun method(message: String) {
                    println(message)
                }
                    
                fun method1(y: Int) {
                    val actions = "Some actions"
                    if (y > 5) {
                        method("y is greater than 5")
                    } else {
                        method("y is less than 5")
                    }
                }
                
                fun method2() {
                    method("Content")
                    println("Content")
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        Assertions.assertEquals(
            methodsList,
            findMethodUsages(methodName),
            "Method $methodName should be called in methods: $methodsList"
        )
    }

    fun testHasProperty() {
        val example = """
            private const val CONSTANT = "some text"
            private val value = 0.5
            val number: Int = 2
           
            fun method() {
                println("Content")
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        var value = "CONSTANT"
        Assertions.assertTrue(hasProperty(value), "There must exist a property with name $value")
        value = "value"
        Assertions.assertTrue(hasProperty(value), "There must exist a property with name $value")
        value = "number"
        Assertions.assertTrue(hasProperty(value), "There must exist a property with name $value")
        Assertions.assertFalse(hasProperty("method"))
        Assertions.assertFalse(hasProperty("Content"))
    }

    fun testHasMethod() {
        val example = """
            private const val CONSTANT = "some text"
            private val value = 0.5
           
            fun method() {
                val actions = "Some actions"
                println("Content")
                println(actions)
            }
                
            fun notMethod() {
                val content = "Content"
                val actions = "Some actions"
                println(actions + content)
            }
                
            fun value() {
                println("Content")
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        var value = "method"
        Assertions.assertTrue(hasMethod(value), "There must exist a method with name $value")
        value = "notMethod"
        Assertions.assertTrue(hasMethod(value), "There must exist a method with name $value")
        value = "value"
        Assertions.assertTrue(hasMethod(value), "There must exist a method with name $value")
        Assertions.assertFalse(hasMethod("CONSTANT"))
        Assertions.assertFalse(hasMethod("Content"))
    }

    fun testHasClass() {
        val example = """
            class ExampleClass {
                fun method() =  { "Some actions" }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        val name = "ExampleClass"
        Assertions.assertTrue(hasClass(name), "There must exist a class with name $name")
        Assertions.assertFalse(hasClass("method"))
        Assertions.assertFalse(hasClass("Class"))
    }

    fun testHasParameter() {
        val example = """
            private const val PARAMETER = "some text"
            
            fun method1(parameter1: Int, parameter2: String) {
                println(parameter2)
            }
            
            fun method2(y: Double) {
                println("Content")
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
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
            private const val CONSTANT = Int.MAX_VALUE
            
            public fun calculateAveragePrice(productPrice: List<Int>): Int? {
                return try {
                    productPrice.sum() / productPrice.count()
                } catch (error: Exception) {
                    PrintWriter(File("Exception.txt"), Charsets.UTF_8).use { it.print(error.toString()) }
                    null
                }
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        Assertions.assertTrue(
            hasExpressionWithParent(expression, parent),
            "There must exist an expression $expression with parent $parent"
        )
    }

    fun testGetMethodCallArguments() {
        val example = """
            fun calculateDogAgeInDogYears() {
                description(""${'"'}
                    text
                ""${'"'}.trimIndent())
            }
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        val methodName = "description"
        val argument = """
            ""${'"'}
                    text
                ""${'"'}.trimIndent()
        """.trimIndent()
        Assertions.assertEquals(argument, getMethodCallArguments(methodName)?.firstOrNull()?.trimIndent()) {
            "There must exist an method call $methodName with arguments $argument"
        }
    }
}
