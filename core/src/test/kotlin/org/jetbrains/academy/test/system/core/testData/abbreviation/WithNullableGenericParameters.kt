package org.jetbrains.academy.test.system.core.testData.abbreviation

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val nullableStringMapKotlinType = TestKotlinType(
    "Map",
    "kotlin.collections.Map<kotlin.String, kotlin.String>",
)

val withNullableGenericParametersTestClass = TestClass(
    "WithNullableGenericParameters",
    "org.jetbrains.academy.test.system.core.testData.abbreviation",
    declaredFields = listOf(
        TestVariable(
            name = "value",
            javaType = "Map",
            kotlinType = nullableStringMapKotlinType,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL
        )
    )
)

@Suppress("unused")
data class WithNullableGenericParameters(val value: Map<String?, String?>)
