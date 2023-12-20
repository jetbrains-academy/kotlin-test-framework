package org.jetbrains.academy.test.system.core;

import org.jetbrains.academy.test.system.core.models.Visibility;
import org.jetbrains.academy.test.system.core.models.classes.ClassType;
import org.jetbrains.academy.test.system.core.models.classes.TestClass;
import org.jetbrains.academy.test.system.core.models.method.TestMethod;
import org.jetbrains.academy.test.system.core.models.variable.TestVariable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;

public class TestJavaClass {
    private static TestClass javaClassTestClass;

    @BeforeAll
    static void beforeAll() {
        javaClassTestClass = new TestClass(
                "JavaClass",
                "org.jetbrains.academy.test.system.core.testData.java",
                Visibility.PUBLIC,
                ClassType.CLASS,
                List.of(
                        new TestVariable(
                                "PUBLIC_CONSTANT",
                                "int",
                                "1",
                                Visibility.PUBLIC,
                                true,
                                false,
                                true
                        ),
                        new TestVariable(
                                "PRIVATE_CONSTANT",
                                "int",
                                "2",
                                Visibility.PRIVATE,
                                true,
                                false,
                                true
                        ),
                        new TestVariable(
                                "publicStaticVar",
                                "int",
                                "3",
                                Visibility.PUBLIC,
                                false,
                                false,
                                true
                        ),
                        new TestVariable(
                                "privateStaticVar",
                                "int",
                                "4",
                                Visibility.PRIVATE,
                                false,
                                false,
                                true
                        ),
                        new TestVariable(
                                "publicVar",
                                "String",
                                "publicVar",
                                Visibility.PUBLIC,
                                false,
                                false,
                                false
                        ),
                        new TestVariable(
                                "privateVar",
                                "String",
                                "privateVar",
                                Visibility.PRIVATE,
                                false,
                                false,
                                false
                        ),
                        new TestVariable(
                                "customClass",
                                "CustomJavaClass",
                                null,
                                Visibility.PUBLIC,
                                false,
                                false,
                                false
                        ),
                        new TestVariable(
                                "primaryConstructorVar",
                                "String",
                                null,
                                Visibility.PUBLIC,
                                false,
                                true,
                                false
                        )
                ),
                List.of(
                        new TestMethod(
                                "publicMethod",
                                "void",
                                emptyList(),
                                Visibility.PUBLIC
                        ),
                        new TestMethod(
                                "privateMethod",
                                "void",
                                emptyList(),
                                Visibility.PRIVATE
                        ),
                        new TestMethod(
                                "calculateSum",
                                "double",
                                List.of(
                                        new TestVariable("a", "double"),
                                        new TestVariable("b", "double")
                                ),
                                Visibility.PUBLIC
                        ),
                        new TestMethod(
                                "getString",
                                "String",
                                List.of(new TestVariable("string", "String")),
                                Visibility.PRIVATE
                        ),
                        new TestMethod(
                                "processList",
                                "List",
                                List.of(new TestVariable("list", "List")),
                                Visibility.PUBLIC
                        )
                ),
                false,
                emptyList(),
                emptyList()
        );
    }

    @Test
    public void javaClassTest() {
        Class<?> clazz = javaClassTestClass.checkBaseDefinition();
        javaClassTestClass.checkFieldsDefinition(clazz, true);
        javaClassTestClass.checkDeclaredMethods(clazz);
    }
}
