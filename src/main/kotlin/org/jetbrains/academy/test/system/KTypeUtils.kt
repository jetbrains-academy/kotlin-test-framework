package org.jetbrains.academy.test.system

import org.jetbrains.academy.test.system.models.TestKotlinType
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

fun KType.checkType(
    kotlinType: TestKotlinType?,
    javaType: String,
    errorMessagePrefix: String,
    toCheckJavaType: Boolean = true
) {
    kotlinType?.let {
        this.checkNullability(kotlinType, errorMessagePrefix)
        it.abbreviation?.let { abr ->
            this.checkAbbreviation(abr, errorMessagePrefix)
        }
    }
    if (toCheckJavaType) {
        val message = "The return type of $errorMessagePrefix must be ${this.javaType.getShortName()}"
        // We have a parametrized type
        if ("<" in this.javaType.toString() && kotlinType?.abbreviation == null) {
            val type = kotlinType?.getTypePrettyString() ?: javaType
            assert(type.lowercase() in this.javaType.toString().lowercase()) { message }
        } else {
            assert(this.javaType.getShortName() == javaType.lowercase()) { message }
        }
    }
}

private fun KType.checkNullability(kotlinType: TestKotlinType, errorMessagePrefix: String) {
    val nullablePrefix = if (!kotlinType.isNullable) "" else "not"
    assert(this.isMarkedNullable == kotlinType.isNullable) { "Error, $errorMessagePrefix must be $nullablePrefix nullable" }
}

private fun KType.checkAbbreviation(abbreviation: String, errorMessagePrefix: String) {
    assert(this.getAbbreviation() == abbreviation) { "The return type for $errorMessagePrefix must contain $abbreviation" }
}

private fun KType.getAbbreviation(): String {
    val separator = " /*"
    val strRepresentation = this.toString()
    if (separator !in strRepresentation) {
        if (arguments.isNotEmpty()) {
            this.arguments.first().type?.toString()?.let {
                return it
            }
        }
        return if ("?" in strRepresentation) {
            strRepresentation.dropLast(1)
        } else {
            strRepresentation
        }
    }
    val abr = strRepresentation.split(separator).first()
    return if ("<" in strRepresentation) {
        "$abr>"
    } else {
        abr
    }
}
