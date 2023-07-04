package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.testData.sam.mySamInterfaceTestClass
import org.junit.jupiter.api.Test

class SamTests {
    @Test
    fun mySamInterfaceTestClassTest() {
        val clazz = mySamInterfaceTestClass.checkBaseDefinition()
        mySamInterfaceTestClass.checkNoConstructors(clazz)
        mySamInterfaceTestClass.checkDeclaredMethods(clazz)
    }
}
