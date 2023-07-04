package org.jetbrains.academy.test.system.models.method

import org.jetbrains.academy.test.system.findMethod
import org.jetbrains.academy.test.system.models.Visibility
import org.jetbrains.academy.test.system.models.classes.TestClass

/**
 * Represents a special class to store any necessary information to invoke a method.
 *
 * @param testClass stores a [TestClass] corresponding to the Java Class<*> in which the given method is defined.
 * @param testMethod stores a [TestMethod] which should be invoked.
 * @param constructorArgumentsTypes stores a list of Java Class<*> types of arguments in the constructor,
 *  which should be called to create the Java class.
 * @param constructorArguments stores arguments for the constructor.
 *  The number of arguments must be the same with the number of [constructorArgumentsTypes].
 */
@Suppress("SpreadOperator")
data class TestMethodInvokeData(
    val testClass: TestClass,
    val testMethod: TestMethod,
    val constructorArgumentsTypes: List<Class<*>> = emptyList(),
    val constructorArguments: List<Any> = emptyList(),
) {
    val clazz = testClass.getJavaClass()
    val method = if (testMethod.visibility == Visibility.PUBLIC) {
        clazz.methods.findMethod(testMethod)
    } else {
        clazz.declaredMethods.findMethod(testMethod)
    }
    val instance: Any = clazz.getConstructor(*constructorArgumentsTypes.toTypedArray())
        .newInstance(*constructorArguments.toTypedArray())
}
