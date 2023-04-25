package org.jetbrains.academy.test.system.models.variable

import org.jetbrains.academy.test.system.getShortName
import org.jetbrains.academy.test.system.models.Visibility
import org.jetbrains.academy.test.system.models.getVisibility
import java.lang.reflect.Field
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaType

/**
 * Represents all field's properties. Only for class-like objects.
 *
 * @param name stores a name of the field.
 * @param visibilityKey stores a visibility key, see [Visibility].
 * @param mutability stores field's mutability, see [VariableMutability].
 * @param javaType stores a short name of java type.
 */
internal data class FieldProperties(
    val name: String,
    val visibilityKey: String?,
    val mutability: VariableMutability?,
    val javaType: String,
) {
    companion object {
        fun buildByKotlinProp(kotlinProp: KProperty<*>) = FieldProperties(
            kotlinProp.name,
            kotlinProp.visibility?.name,
            kotlinProp.getVariableMutability(),
            kotlinProp.returnType.javaType.getShortName(),
        )

        fun buildByJavaField(field: Field) = FieldProperties(
            field.name,
            field.getVisibility()?.name,
            VariableMutability.JAVA_MUTABILITY,
            field.type.getShortName(),
        )
    }

    fun checkProperties(variable: TestVariable) {
        assert(name == variable.name) { "The field name must be: ${variable.name}" }
        val visibilityErrorMessage = variable.visibility?.let {
            "The visibility of the field ${variable.name} must be ${it.key}"
        } ?: "The filed ${variable.name} should not have any modifiers"
        assert(visibilityKey?.lowercase() == variable.visibility?.key) { visibilityErrorMessage }
        val mutabilityErrorMessage = variable.mutability?.let {
            "The field ${variable.name} must be ${it.key}"
        } ?: "The filed ${variable.name} should not have val or var key words"
        assert(mutability.compareWith(variable.mutability)) { mutabilityErrorMessage }
        assert(javaType == variable.javaType.lowercase()) { "The return type of the field ${variable.name} must be $javaType" }
    }
}