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
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = false,
            isConst = true
        ),
        TestVariable(
            name = "PRIVATE_CONSTANT",
            javaType = "int",
            value = "2",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = false,
            isConst = true
        ),
        TestVariable(
            name = "publicStaticVar",
            javaType = "int",
            value = "3",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "privateStaticVar",
            javaType = "int",
            value = "4",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = false,
            isStatic = true
        ),
        TestVariable(
            name = "publicVar",
            javaType = "String",
            value = "publicVar",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = false,
        ),
        TestVariable(
            name = "privateVar",
            javaType = "String",
            value = "privateVar",
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = false,
        ),
        TestVariable(
            name = "customClass",
            javaType = "CustomJavaClass",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = false,
        ),
        TestVariable(
            name = "primaryConstructorVar",
            javaType = "String",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = true,
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
