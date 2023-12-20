package org.jetbrains.academy.test.system.core.testData.java

import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.method.TestMethod
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val javaClassTestClass = TestClass(
    "JavaClass",
    "org.jetbrains.academy.test.system.core.testData.java",
    declaredFields = listOf(
        TestVariable(
            name = "PUBLIC_CONSTANT",
            javaType = "int",
            value = "1",
            visibility = Visibility.PUBLIC,
            isFinal = true,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "PRIVATE_CONSTANT",
            javaType = "int",
            value = "2",
            visibility = Visibility.PRIVATE,
            isFinal = true,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "publicStaticVar",
            javaType = "int",
            value = "3",
            visibility = Visibility.PUBLIC,
            isFinal = false,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "privateStaticVar",
            javaType = "int",
            value = "4",
            visibility = Visibility.PRIVATE,
            isFinal = false,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "publicVar",
            javaType = "String",
            value = "publicVar",
            visibility = Visibility.PUBLIC,
            isFinal = false,
            isInPrimaryConstructor = false,
            isStatic = false
        ),
        TestVariable(
            name = "privateVar",
            javaType = "String",
            value = "privateVar",
            visibility = Visibility.PRIVATE,
            isFinal = false,
            isInPrimaryConstructor = false,
            isStatic = false
        ),
        TestVariable(
            name = "customClass",
            javaType = "CustomJavaClass",
            value = null,
            visibility = Visibility.PUBLIC,
            isFinal = false,
            isInPrimaryConstructor = false,
            isStatic = false
        ),
        TestVariable(
            name = "primaryConstructorVar",
            javaType = "String",
            value = null,
            visibility = Visibility.PUBLIC,
            isFinal = false,
            isInPrimaryConstructor = true,
            isStatic = false
        ),
    ),
    customMethods = listOf(
        TestMethod(
            name = "publicMethod",
            returnTypeJava = "void",
            arguments = emptyList(),
            visibility = Visibility.PUBLIC
        ),
        TestMethod(
            name = "privateMethod",
            returnTypeJava = "void",
            arguments = emptyList(),
            visibility = Visibility.PRIVATE
        ),
        TestMethod(
            name = "calculateSum",
            returnTypeJava = "double",
            arguments = listOf(
                TestVariable(
                    name = "a",
                    javaType = "double",
                ),
                TestVariable(
                    name = "b",
                    javaType = "double",
                ),
            ),
            visibility = Visibility.PUBLIC
        ),
        TestMethod(
            name = "getString",
            returnTypeJava = "String",
            arguments = listOf(
                TestVariable(
                    name = "string",
                    javaType = "String",
                ),
            ),
            visibility = Visibility.PRIVATE
        ),
        TestMethod(
            name = "processList",
            returnTypeJava = "List",
            arguments = listOf(
                TestVariable(
                    name = "list",
                    javaType = "List"
                ),
            ),
            visibility = Visibility.PUBLIC
        ),
    )
)
