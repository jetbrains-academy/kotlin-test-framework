package org.jetbrains.academy.test.system.ij.kotlin

import org.junit.jupiter.api.Assertions
import org.jetbrains.academy.test.kotlin.test.BaseIjTestClass

class BaseIjTestClassTests : BaseIjTestClass() {

    fun testFindMethodsWithContent() {
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
        val content = """
            val actions = "Some actions"
            println("Content")
            println(actions)
        """.trimIndent()
        val methodName = "method1"
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

    fun testFindMethodsWithContentWithBrokenFormatting() {
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
        val content = """
            val actions = "Some actions"
            println("Content")
            println(actions)
        """.trimIndent()
        val methodName = "method1"
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

    fun testFindMethodUsages() {
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
        var methodName = "method(\"Content\")"
        var methodsList = listOf("innerFunction", "method2")
        Assertions.assertEquals(
            methodsList,
            findMethodUsages(methodName),
            "Method $methodName should be called in methods: $methodsList"
        )
        methodName = "method(\"y is greater than 5\")"
        methodsList = listOf("method1")
        Assertions.assertEquals(
            methodsList,
            findMethodUsages(methodName),
            "Method $methodName should be called in methods: $methodsList"
        )
        methodName = "method(\"y is less than 5\")"
        methodsList = listOf("method1")
        Assertions.assertEquals(
            methodsList,
            findMethodUsages(methodName),
            "Method $methodName should be called in methods: $methodsList"
        )
        methodName = "method(content)"
        methodsList = listOf()
        Assertions.assertEquals(methodsList, findMethodUsages(methodName), "Method $methodName should not be called")
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

    fun testHasExpressionWithParent() {
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
        var expression: String = "productPrice.sum()"
        var parent: String? = "productPrice.sum() / productPrice.count()"
        Assertions.assertTrue(
            hasExpressionWithParent(expression, parent),
            "There must exist an expression $expression with parent $parent"
        )
        expression = "File(\"Exception.txt\")"
        parent = "File(\"Exception.txt\")"
        Assertions.assertTrue(
            hasExpressionWithParent(expression, parent),
            "There must exist an expression $expression with parent $parent"
        )
        expression = "PrintWriter(File(\"Exception.txt\"), Charsets.UTF_8).use { it.print(error.toString()) }"
        parent = "calculateAveragePrice"
        Assertions.assertTrue(
            hasExpressionWithParent(expression, parent, true),
            "There must exist an expression $expression with parent $parent"
        )
        expression = "Int.MAX_VALUE"
        parent = "val CONSTANT = Int.MAX_VALUE"
        Assertions.assertFalse(hasExpressionWithParent(expression, parent))
        expression = "Int.MAX_VALUE"
        parent = null
        Assertions.assertTrue(
            hasExpressionWithParent(expression, parent, true),
            "There must exist an expression $expression with parent $parent"
        )
    }
}
