package org.jetbrains.academy.test.system.core.testData.abbreviation

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val withNullableTestClass = TestClass(
    "WithNullable",
    "org.jetbrains.academy.test.system.core.testData.abbreviation",
    declaredFields = listOf(
        TestVariable(
            name = "name",
            javaType = "String",
            kotlinType = TestKotlinType("String?",
                "kotlin.String",
                isNullable = true
            ),
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL
        )
    )
)

@Suppress("unused")
data class WithNullable(val name: String?)
