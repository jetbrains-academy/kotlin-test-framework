package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.testData.abbreviation.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.Arguments

class AbbreviationTests {
    companion object {
        @JvmStatic
        fun testData() = listOf(
            Arguments.of(withNullableTestClass),
            Arguments.of(withTypeAliasTestClass),
            Arguments.of(withGenericTypeAliasTestClass),
            Arguments.of(withDoubleTypeAliasTestClass),
            Arguments.of(withNullableGenericParametersTestClass)
        )
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun testAbbreviation(testClass: TestClass) {
        val clazz = testClass.checkBaseDefinition()
        testClass.checkFieldsDefinition(clazz)
        testClass.checkDeclaredMethods(clazz)
    }
}
