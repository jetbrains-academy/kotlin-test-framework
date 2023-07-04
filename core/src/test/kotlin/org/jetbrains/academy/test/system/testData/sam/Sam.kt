package org.jetbrains.academy.test.system.testData.sam

import org.jetbrains.academy.test.system.models.TestKotlinType
import org.jetbrains.academy.test.system.models.classes.ClassType
import org.jetbrains.academy.test.system.models.classes.TestClass
import org.jetbrains.academy.test.system.models.method.TestMethod

fun interface MySamInterface {
    fun samMethod(): List<String>
}

val mySamInterfaceTestClass = TestClass(
    "MySamInterface",
    "org.jetbrains.academy.test.system.testData.sam",
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
