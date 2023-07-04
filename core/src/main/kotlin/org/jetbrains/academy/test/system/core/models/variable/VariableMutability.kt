package org.jetbrains.academy.test.system.core.models.variable

import java.lang.reflect.Field
import kotlin.reflect.KProperty

/**
 * Represents mutability of variables.
 * If we recreate mutability from java sources, e.g. from [Field]
 * we use the [VariableMutability.JAVA_MUTABILITY] value.
 */
enum class VariableMutability(val key: String) {
    VAL("val"),
    VAR("var"),
    JAVA_MUTABILITY(""),
    ;
}

fun KProperty<*>.getVariableMutability(): VariableMutability? {
    val strRepresentation = this.toString()
    if (VariableMutability.VAL.key in strRepresentation) {
        return VariableMutability.VAL
    }
    if (VariableMutability.VAR.key in strRepresentation) {
        return VariableMutability.VAR
    }
    return null
}

fun VariableMutability?.compareWith(expected: VariableMutability?): Boolean {
    if (this == VariableMutability.JAVA_MUTABILITY) {
        // We can not define the mutability :(
        return true
    }
    return this == expected
}
