package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.models.method.TestMethod
import java.lang.reflect.Method

fun Method.invokeWithoutArgs(
    clazz: Class<*>,
    isPrivate: Boolean = false,
    obj: Any? = null,
): Any = invokeWithArgs(clazz = clazz, isPrivate = isPrivate, obj = obj)


fun Method.invokeWithArgs(
    vararg args: Any,
    clazz: Class<*>,
    isPrivate: Boolean = false,
    obj: Any? = null,
): Any {
    if (isPrivate) {
        this.isAccessible = true
    }
    return obj?.let { invoke(it, *args) } ?: invoke(clazz, *args)
}

private fun List<Method>.filterByCondition(errorMessage: String, condition: (Method) -> Boolean): List<Method> {
    val filteredByCondition = this.filter { condition(it) }
    if (filteredByCondition.isEmpty()) {
        assert(false) { errorMessage }
    }
    return filteredByCondition
}

@Suppress("MaxLineLength")
fun Array<Method>.findMethod(method: TestMethod, customErrorMessage: String? = null): Method {
    val filteredByName =
        this.toList().filterByCondition(customErrorMessage ?: "The method ${method.prettyString()} is missed. Check it's name and access modifier properly.") {
            if (method.hasGeneratedPartInName) {
                method.name in it.name
            } else {
                it.name == method.name
            }
        }
    val returnTypeJava = method.returnTypeJava ?: method.returnType.type
    val filteredByType =
        filteredByName.filterByCondition(customErrorMessage ?: "The method ${method.name} should have the return type ${method.returnType.getTypePrettyString()}") {
            it.returnType.name.getShortName().lowercase() == returnTypeJava.lowercase()
        }
    val filteredByArgumentsCount =
        filteredByType.filterByCondition(customErrorMessage ?: "The method ${method.name} should have ${method.arguments.size} arguments") { it.parameterCount == method.arguments.size }
    require(filteredByArgumentsCount.size == 1) { customErrorMessage ?: "The method ${method.prettyString()} is missed. Check it's arguments properly." }
    val m = filteredByArgumentsCount.first()
    val params = m.parameterTypes.map { it.name.getShortName().lowercase() }
    val args = method.arguments.map { it.javaType.lowercase() }
    assert(params == args) { customErrorMessage ?: "The method ${method.name} should have ${method.arguments.size} arguments: $params. The full signature is: ${method.prettyString()}." }
    return m
}
