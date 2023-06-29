package org.jetbrains.academy.test.system.models

/**
 * Represents a kotlin type. Consider several examples:
 *
 * 1) Consider a type alias `Identifier`:
 * ```
 * package org.jetbrains.academy.test.system.models.variable
 *
 * typealias Identifier = Int
 * ```
 * The [TestKotlinType] is
 * ```
 * KotlinType("Int", "org.jetbrains.academy.test.system.models.variable.Identifier")
 * ```
 *
 * 2) For a list of numbers `List<Int>` the [TestKotlinType] is
 * ```
 * KotlinType("List", params = listOf("kotlin.Int"))
 * ```
 *
 * 3) It also works with custom user's types as parameters:
 * ```
 * package org.jetbrains.academy.test.system.models.variable
 *
 * data class MyClass(val a: Int)
 * ```
 * The [TestKotlinType] for a list of `MyClass` is
 * ```
 * KotlinType("List", params = listOf("org.jetbrains.academy.test.system.models.variable.MyClass"))
 * ```
 *
 * @param type is a short type's name.
 * @param abbreviation is a Kotlin-specific representation, e.g. a fqName of a typealias or a fqName of an inline class.
 * @param isNullable indicates if this type is nullable.
 * @param params stores a list of fqNames of type parameters (only for generics).
 */
data class TestKotlinType(
    val type: String,
    val abbreviation: String? = null,
    val isNullable: Boolean = false,
    val params: List<String> = emptyList(),
) {
    fun getTypePrettyString() = abbreviation ?: run {
        if (params.isNotEmpty()) {
            "$type<${params.joinToString(", ")}>"
        } else {
            type
        }
    }
}
