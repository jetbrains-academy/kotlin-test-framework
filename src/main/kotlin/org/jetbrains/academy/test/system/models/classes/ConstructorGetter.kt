package org.jetbrains.academy.test.system.models.classes

import kotlin.jvm.internal.DefaultConstructorMarker

/**
 * A helper to find and return constructors for classes.
 *
 * @param parameterTypes stores a list of Java Class<*> types of arguments in the constructor,
 *  which should be called to create the Java class.
 * @param defaultParameterTypes stores a list of Java Class<*> default types of arguments in the constructor,
 *  which should be called to create the Java class.
 * @param toAddDefaultConstructorMarker indicates if we need to add [DefaultConstructorMarker] class
 *  into the list with parameters.
 */
data class ConstructorGetter(
    val parameterTypes: List<Class<*>> = emptyList(),
    val defaultParameterTypes: List<Class<*>> = emptyList(),
    val toAddDefaultConstructorMarker: Boolean = false
) {
    @Suppress("SpreadOperator")
    fun getConstructorWithDefaultArguments(clazz: Class<*>) = try {
        val parameters =
            (parameterTypes + defaultParameterTypes.map { listOf(it, Int::class.java) }.flatten()).toMutableList()
        if (defaultParameterTypes.isNotEmpty() || toAddDefaultConstructorMarker) {
            parameters.add(DefaultConstructorMarker::class.java)
        }
        clazz.getConstructor(*parameters.toTypedArray())
    } catch (e: NoSuchMethodException) {
        null
    }
}
