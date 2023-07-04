package org.jetbrains.academy.test.system.core.models.variable

import org.jetbrains.academy.test.system.core.getShortName
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.asVisibility
import org.jetbrains.academy.test.system.core.models.getVisibility
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaType

/**
 * Represents all field's properties. Only for class-like objects.
 *
 * @param name stores a name of the field.
 * @param visibility stores field's visibility, see [Visibility].
 * @param mutability stores field's mutability, see [VariableMutability].
 * @param javaType stores a short name of java type.
 */
internal data class FieldProperties(
    val name: String,
    val visibility: Visibility?,
    val mutability: VariableMutability?,
    val javaType: String,
) {
    companion object {
        fun buildByKotlinProp(kotlinProp: KProperty<*>) = FieldProperties(
            kotlinProp.name,
            kotlinProp.visibility?.asVisibility(),
            kotlinProp.getVariableMutability(),
            kotlinProp.returnType.javaType.getShortName(),
        )

        private fun Field.getMutabilityByJavaField() = if (Modifier.isFinal(this.modifiers)) {
            VariableMutability.VAL
        } else {
            VariableMutability.VAR
        }

        fun buildByJavaField(field: Field) = FieldProperties(
            field.name,
            field.getVisibility(),
            field.getMutabilityByJavaField(),
            field.type.getShortName(),
        )
    }

    fun checkProperties(variable: TestVariable, toCheckMutability: Boolean) {
        assert(name == variable.name) { "The field name must be: ${variable.name}" }
        val visibilityErrorMessage = variable.visibility?.let {
            "The visibility of the field ${variable.name} must be ${it.key}"
        } ?: "The filed ${variable.name} should not have any modifiers"
        assert(visibility?.key?.lowercase() == variable.visibility?.key) { visibilityErrorMessage }
        if (toCheckMutability) {
            val mutabilityErrorMessage = variable.mutability?.let {
                "The field ${variable.name} must be ${it.key}"
            } ?: "The filed ${variable.name} should not have val or var key words"
            assert(mutability.compareWith(variable.mutability)) { mutabilityErrorMessage }
        }
        assert(javaType == variable.javaType.lowercase()) { "The return type of the field ${variable.name} must be ${variable.javaType.lowercase()}" }
    }
}
