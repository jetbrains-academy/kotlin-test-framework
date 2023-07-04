package org.jetbrains.academy.test.system.core.testData.sam

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.classes.ClassType
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.method.TestMethod

fun interface MySamInterface {
    fun samMethod(): List<String>
}

val mySamInterfaceTestClass = TestClass(
    "MySamInterface",
    "org.jetbrains.academy.test.system.core.testData.sam",
    classType = ClassType.SAM_INTERFACE,
    customMethods = listOf(
        TestMethod(
            "samMethod",
            TestKotlinType(
                "List",
                params = listOf("java.lang.string")
            ),
            returnTypeJava = "List",
        ),
    ),
)
