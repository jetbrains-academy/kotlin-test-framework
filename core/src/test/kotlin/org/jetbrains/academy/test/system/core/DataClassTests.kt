package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.ConstructorGetter
import org.jetbrains.academy.test.system.core.models.method.TestMethod
import org.jetbrains.academy.test.system.core.models.method.TestMethodInvokeData
import org.jetbrains.academy.test.system.core.testData.dataClass.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DataClassTests {
    companion object {
        @JvmStatic
        fun sumMethodTestData() = listOf(
            // a, b, expected
            Arguments.of(2, 4, 6),
            Arguments.of(0, -1, -1),
            Arguments.of(0, 0, 0),
        )
    }

    @Test
    fun dataClassTest() {
        val clazz = dataClassTestClass.checkBaseDefinition()
        dataClassTestClass.checkFieldsDefinition(clazz)
        val constructor = dataClassTestClass.checkConstructors(
            clazz, listOf(
                ConstructorGetter(
                    parameterTypes = listOf(String::class.java, MyTypeAlias::class.java, Int::class.java),
                ),
                ConstructorGetter(
                    parameterTypes = listOf(String::class.java, MyTypeAlias::class.java),
                    defaultParameterTypes = listOf(Int::class.java)
                ),
            )
        )
        // Just check if the constructor works well
        constructor.newInstance("string", "aliasToString", 5)
        dataClassTestClass.checkDeclaredMethods(clazz)

        // Check if the publicVal stores the necessary value
        for (i in 0..100) {
            val publicVal = "string$i"
            val instance = constructor.newInstance(publicVal, "aliasToString", 5)
            val publicValGetterMethod = clazz.methods.findMethod(publicValGetterMethod)
            val publicValActual = dataClassTestClass.invokeMethodWithoutArgs(clazz, instance, publicValGetterMethod)
            assert(publicVal == publicValActual.toString()) { "The instance of the class ${dataClassTestClass.name} must have value $publicVal in the publicVal field if it was passed into the constructor" }
        }
    }

    @ParameterizedTest
    @MethodSource("sumMethodTestData")
    fun publicFooWithArgumentsMethodTest(a: Int, b: Int, expected: Int) {
        sumMethodTest(publicFooWithArgumentsMethod, a, b, expected)
    }

    @ParameterizedTest
    @MethodSource("sumMethodTestData")
    fun privateFooWithArgumentsMethodTest(a: Int, b: Int, expected: Int) {
        sumMethodTest(privateFooWithArgumentsMethod, a, b, expected)
    }

    private fun sumMethodTest(sumMethod: TestMethod, a: Int, b: Int, expected: Int) {
        val invokeData = sumMethod.getInvokeData()
        val actualSum = dataClassTestClass.invokeMethodWithArgs(
            a, b,
            invokeData = invokeData,
            isPrivate = sumMethod.visibility == Visibility.PRIVATE
        ).toString()
        assert(expected.toString() == actualSum) { "For a = $a and b = $b the method ${sumMethod.name} must return $expected." }
    }

    private fun TestMethod.getInvokeData() = TestMethodInvokeData(
        dataClassTestClass,
        this,
        constructorArguments = listOf("string", "aliasToString", 5),
        constructorArgumentsTypes = listOf(String::class.java, MyTypeAlias::class.java, Int::class.java)
    )

    private fun constMethodTest(constMethod: TestMethod) {
        val invokeData = constMethod.getInvokeData()
        val actualConst = dataClassTestClass.invokeMethodWithoutArgs(
            invokeData = invokeData,
            isPrivate = constMethod.visibility == Visibility.PRIVATE
        ).toString()
        assert("5" == actualConst) { "The method ${constMethod.name} must return 5." }
    }

    @Test
    fun publicFooWithoutArgumentsMethodTest() {
        constMethodTest(publicFooWithoutArgumentsMethod)
    }

    @Test
    fun privateFooWithoutArgumentsMethodTest() {
        constMethodTest(privateFooWithoutArgumentsMethod)
    }
}
