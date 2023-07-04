package org.jetbrains.academy.test.system.core.testData.companion

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability
import org.jetbrains.academy.test.system.core.testData.sam.MySamInterface

@Suppress("UtilityClassWithPublicConstructor")
class MyClass {
    companion object {
        private val samParent = MySamInterface {
            listOf("one", "two", "three")
        }
    }
}

val myClassTestClass = TestClass(
    "MyClass",
    "org.jetbrains.academy.test.system.core.testData.companion",
    declaredFields = listOf(
        // All properties from companion are defined inside the base class
        TestVariable(
            name = "samParent",
            javaType = "MySamInterface",
            kotlinType = TestKotlinType(
                "org.jetbrains.academy.test.system.core.testData.sam.MySamInterface",
            ),
            // Because it is inside companion object
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
            isStatic = true,
        ),
    ),
)

internal val myClassTestClassTestClass = TestClass(
    "MyClass\$Companion",
    "org.jetbrains.academy.test.system.core.testData.companion"
)
