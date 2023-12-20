package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.testData.java.javaClassTestClass
import org.junit.jupiter.api.Test

class JavaClassTests {

    @Test
    fun javaClassTest() {
        val clazz = javaClassTestClass.checkBaseDefinition()
        javaClassTestClass.checkFieldsDefinition(clazz)
        javaClassTestClass.checkDeclaredMethods(clazz)
    }
}
