package org.jetbrains.academy.test.system.core.testData.abbreviation

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

val typeAliasTestKotlinType = TestKotlinType(
    "String",
    "org.jetbrains.academy.test.system.core.testData.abbreviation.TypeAlias"
)

val withTypeAliasTestClass = TestClass(
    "WithTypeAlias",
    "org.jetbrains.academy.test.system.core.testData.abbreviation",
    declaredFields = listOf(
        TestVariable(
            name = "name",
            javaType = "String",
            kotlinType = typeAliasTestKotlinType,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL
        )
    )
)

typealias TypeAlias = String

@Suppress("unused")
data class WithTypeAlias(val name: TypeAlias)
