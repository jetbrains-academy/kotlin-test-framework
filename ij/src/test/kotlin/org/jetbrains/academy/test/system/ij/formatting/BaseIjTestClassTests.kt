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
        val content = "println(\"Content\")"
        assert(listOf("method1", "method3").equals(findMethodsWithContent(content)))
    }

    fun testHasConstantWithGivenValue() {
        val example = """
            private const val CONSTANT1 = "some text"
            private val notConstant = 0.5
            const val CONSTANT2 = 2
        """.trimIndent()
        myFixture.configureByText("Task.kt", example)
        assert(hasConstantWithGivenValue("\"some text\""))
        assert(hasConstantWithGivenValue("2"))
        assert(!hasConstantWithGivenValue("0.5"))
    }
}
