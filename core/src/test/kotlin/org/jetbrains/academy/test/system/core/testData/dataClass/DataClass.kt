@file:Suppress("FunctionOnlyReturningConstant", "VarCouldBeVal")

package org.jetbrains.academy.test.system.core.testData.dataClass

import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.classes.TestClass
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.jetbrains.academy.test.system.core.models.variable.VariableMutability

typealias MyTypeAlias = String

@JvmInline
value class MyInlineClass(val property: String)

data class DataClass(
    val publicVal: String,
    var publicVar: MyTypeAlias,
    private val privateVal: Int = 5,
) {
    private var privateVar: MyInlineClass? = null

    fun publicFooWithoutArguments() = 5

    fun publicFooWithArguments(a: Int, b: Int) = a + b

    private fun privateFooWithoutArguments() = 5

    private fun privateFooWithArguments(a: Int, b: Int) = a + b
}


val dataClassTestClass = TestClass(
    "DataClass",
    "org.jetbrains.academy.test.system.core.testData.dataClass",
    isDataClass = true,
    declaredFields = listOf(
        TestVariable(
            name = "publicVal",
            javaType = "string",
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = true,
        ),
        TestVariable(
            name = "publicVar",
            javaType = "string",
            kotlinType = myTypeAliasKotlinType,
            visibility = Visibility.PUBLIC,
            mutability = VariableMutability.VAR,
            isInPrimaryConstructor = true,
        ),
        TestVariable(
            name = "privateVal",
            javaType = "int",
            kotlinType = TestKotlinType("kotlin.Int"),
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAL,
            isInPrimaryConstructor = true,
        ),
        TestVariable(
            name = "privateVar",
            javaType = "string",
            kotlinType = myInlineClassKotlinType,
            visibility = Visibility.PRIVATE,
            mutability = VariableMutability.VAR,
        ),
    ),
    customMethods = listOf(
        publicFooWithoutArgumentsMethod,
        publicFooWithArgumentsMethod,
        privateFooWithoutArgumentsMethod,
        privateFooWithArgumentsMethod
    )
)
