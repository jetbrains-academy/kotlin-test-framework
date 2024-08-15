package org.jetbrains.academy.test.system.core.testData.abbreviation

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val dictionaryTestKotlinType = TestKotlinType(
    "Map<String, String>",
    "org.jetbrains.academy.test.system.core.testData.abbreviation.Dictionary<kotlin.String>"
)

val withGenericTypeAliasTestClass = TestClass(
    "WithGenericTypeAlias",
    "org.jetbrains.academy.test.system.core.testData.abbreviation",
    declaredFields = listOf(
        TestVariable(
            name = "value",
            javaType = "Map",
            kotlinType = dictionaryTestKotlinType,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL
        )
    )
)

typealias Dictionary<T> = Map<String, T>

@Suppress("unused")
data class WithGenericTypeAlias(val value: Dictionary<String>)
