package org.jetbrains.academy.test.system.testData.dataClass

import org.jetbrains.academy.test.system.models.TestKotlinType

val myTypeAliasKotlinType = TestKotlinType(
    "String",
    "org.jetbrains.academy.test.system.testData.dataClass.MyTypeAlias"
)

val myInlineClassKotlinType = TestKotlinType(
    "String",
    "org.jetbrains.academy.test.system.testData.dataClass.MyInlineClass",
    isNullable = true
)
