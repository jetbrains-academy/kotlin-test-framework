@file:Suppress("ForbiddenComment")

package org.jetbrains.academy.test.system.core.models.variable

import org.jetbrains.academy.test.system.core.checkType
import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.throwInternalLibError
import org.junit.jupiter.api.Assertions
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.jvm.kotlinProperty

/**
 * Represents a variable in code.
 * Can be a top-level variable, a function or a constructor parameter, a field in a class, etc.
 *
 * @param name represents a variable name.
 * @param javaType represents a short java name.
 * @param value represents the value of a variable, such as a constant.
 * @param kotlinType represents the value of a variable, such as a constant represents a [TestKotlinType],
 *  usually is used in cases when javaType and kotlinType are differ.
 * @param visibility represents a variable [Visibility].
 * @param mutability represents a [VariableMutability].
 * @param isInPrimaryConstructor indicates if this variable is used in the primary constructor (only for classes).
 * @param isStatic indicates if the variable is static, e.g. is located in a companion object.
 * @param isConst indicates if the variable has the const modifier.
 */
data class TestVariable(
    val name: String,
    val javaType: String,
    val value: String? = null,
    val kotlinType: TestKotlinType? = null,
    val visibility: Visibility? = null,
    val mutability: VariableMutability? = null,
    val isInPrimaryConstructor: Boolean = false,
    val isStatic: Boolean = false,
    val isConst: Boolean = false,
    // TODO: add nullability?
) {
    constructor(
        name: String,
        javaType: String,
        value: String?,
        visibility: Visibility?,
        isFinal: Boolean,
        isInPrimaryConstructor: Boolean,
        isStatic: Boolean
    ) : this(
        name = name,
        javaType = javaType,
        value = value,
        kotlinType = null,
        visibility = visibility,
        mutability = if (isFinal) VariableMutability.VAL else VariableMutability.VAR,
        isInPrimaryConstructor = isInPrimaryConstructor,
        isStatic = isStatic,
        isConst = isStatic && isFinal
    )

    constructor(name: String, javaType: String) : this(name, javaType, null, null, null, null, false, false, false)

    private fun getTypePrettyString() = kotlinType?.getTypePrettyString() ?: javaType

    fun prettyString(): String {
        val currentMutability = mutability?.key ?: ""
        val prefix = visibility?.let {
            "${visibility.key} $currentMutability "
        } ?: ""
        return "$prefix$name: ${getTypePrettyString()}"
    }

    fun checkField(field: Field, toCheckMutability: Boolean) {
        val commonProp =
            field.kotlinProperty?.let { FieldProperties.buildByKotlinProp(it) } ?: FieldProperties.buildByJavaField(
                field
            )
        commonProp.checkProperties(this, toCheckMutability)
        if (isStatic) {
            Assertions.assertTrue(
                Modifier.isStatic(field.modifiers),
                "The field $name must be defined into an object or a companion object."
            )
        }
        if (isConst) {
            val errorMessage = "The field $name must be a const value."
            Assertions.assertTrue(Modifier.isFinal(field.modifiers), errorMessage)
            field.kotlinProperty?.isConst?.let {
                Assertions.assertTrue(it, errorMessage)
            }
        }
        field.kotlinProperty?.returnType?.checkType(
            kotlinType, javaType, "the field $name", false
        )
    }
}

fun TestVariable.variableDefTemplateBase() = "val ${this.name} = ${this.value}"

fun TestVariable.variableDefTemplateWithType() = "val ${this.name}: ${this.javaType} = ${this.value}"

fun TestVariable.isVariableExist(fileContent: String): Boolean {
    val baseDef = variableDefTemplateBase()
    val defWithType = variableDefTemplateWithType()
    if (!(baseDef in fileContent || defWithType in fileContent)) {
        error(
            "The code should contains a definition of the ${this.name} variable! " + "Please, add <$baseDef> or <$defWithType> code in your solution."
        )
    }
    return true
}

fun checkListOfVariables(sourceCodeFile: File, variables: List<TestVariable>) {
    if (sourceCodeFile.exists()) {
        val content = sourceCodeFile.readText()
        for (variable in variables) {
            Assertions.assertTrue(variable.isVariableExist(content))
        }
    } else {
        // TODO: log some errors?
        throwInternalLibError()
    }
}
