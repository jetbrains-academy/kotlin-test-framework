package org.jetbrains.academy.test.system.core.testData.abbreviation

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val identifierMapKotlinType = TestKotlinType(
    "Map",
    "kotlin.collections.Map<org.jetbrains.academy.test.system.core.testData.abbreviation.Identifier, org.jetbrains.academy.test.system.core.testData.abbreviation.Identifier>"
)

val withDoubleTypeAliasTestClass = TestClass(
    "WithDoubleTypeAlias",
    "org.jetbrains.academy.test.system.core.testData.abbreviation",
    declaredFields = listOf(
        TestVariable(
            name = "value",
            javaType = "Map",
            kotlinType = identifierMapKotlinType,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL
        )
    )
)

typealias Identifier = Int

@Suppress("unused")
data class WithDoubleTypeAlias(val value: Map<Identifier, Identifier>)
