package org.jetbrains.academy.test.system.core.testData.dataClass

import org.jetbrains.academy.test.system.core.models.TestKotlinType

val myTypeAliasKotlinType = TestKotlinType(
    "String",
    "org.jetbrains.academy.test.system.core.testData.dataClass.MyTypeAlias"
)

val myInlineClassKotlinType = TestKotlinType(
    "String",
    "org.jetbrains.academy.test.system.core.testData.dataClass.MyInlineClass",
    isNullable = true
)
