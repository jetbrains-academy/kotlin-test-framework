package org.jetbrains.academy.test.system.ij.formatting

import org.jetbrains.academy.test.system.test.BaseIjTestClass

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
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
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
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
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
        assert(listOf(methodName).equals(findMethodsWithContent(content))) {
            "The name of a method with this content \n $content \n must be $methodName"
        }
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
        assertThrows(IllegalArgumentException::class.java) { findMethodsWithContent(content) }
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
          private const val CONSTANT1 = "some text"
             private val notConstant = 0.5
           const val CONSTANT2 = 2
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        var value = "\"some text\""
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        value = "2"
        assert(hasConstantWithGivenValue(value)) { "There must exist a constant with value $value" }
        assertFalse(hasConstantWithGivenValue("0.5"))
    }
}
