package org.jetbrains.academy.test.system

import org.jetbrains.academy.test.system.models.classes.ConstructorGetter
import org.jetbrains.academy.test.system.testData.companion.myClassTestClass
import org.jetbrains.academy.test.system.testData.companion.myClassTestClassTestClass
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
