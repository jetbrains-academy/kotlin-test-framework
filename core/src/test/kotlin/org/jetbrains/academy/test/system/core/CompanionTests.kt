package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.models.classes.ConstructorGetter
import org.jetbrains.academy.test.system.core.testData.companion.myClassTestClass
import org.jetbrains.academy.test.system.core.testData.companion.myClassTestClassTestClass
import org.junit.jupiter.api.Test

class CompanionTests {
    @Test
    fun myClassTest() {
        val clazz = myClassTestClass.checkBaseDefinition()
        val companion = myClassTestClassTestClass.checkBaseDefinition()
        myClassTestClassTestClass.checkDeclaredMethods(companion)
        myClassTestClass.checkFieldsDefinition(clazz, false)
        myClassTestClass.checkConstructors(
            clazz, listOf(
                ConstructorGetter(),
            )
        )
        myClassTestClass.checkDeclaredMethods(clazz)
    }
}
