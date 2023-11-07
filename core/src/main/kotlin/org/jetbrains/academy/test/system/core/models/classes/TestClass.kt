package org.jetbrains.academy.test.system.core.models.classes

import org.jetbrains.academy.test.system.core.*
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.method.TestMethod
import org.jetbrains.academy.test.system.core.models.method.TestMethodInvokeData
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.junit.jupiter.api.Assertions
import java.lang.reflect.Constructor
import java.lang.reflect.Method

/**
 * Represents a class-like object.
 *
 * @param name stores a short name of the class.
 * @param classPackage stores a package where the class was defined.
 * @param visibility stores [Visibility].
 * @param classType stores [ClassType], like interface, object, etc.
 * @param declaredFields stores a list of [TestVariable], which represents all declared fields in the class.
 * @param customMethods stores a list of [TestMethod], which represents declared methods by a user in the class.
 * @param isDataClass indicates if the class is a data class.
 * @param declaredEnumEntries stores a list of [TestVariable], which represents all enum entries in the enum class.
 * @param interfaces stores a list of [TestClass], which represents all base interfaces for the class.
 */
data class TestClass(
    val name: String = "MainKt",
    val classPackage: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
    val classType: ClassType = ClassType.CLASS,
    val declaredFields: List<TestVariable> = emptyList(),
    val customMethods: List<TestMethod> = emptyList(),
    val isDataClass: Boolean = false,
    val declaredEnumEntries: List<TestVariable> = emptyList(),
    val interfaces: List<TestClass> = emptyList(),
) {
    fun getFullName() = classPackage?.let {
        "$it.$name"
    } ?: name

    private fun getBaseDefinition() = "${visibility.key} ${classType.key} $name"

    private fun getFieldsListPrettyString() = declaredFields.joinToString { it.prettyString() }

    fun checkBaseDefinition(): Class<*> {
        val clazz = this.findClassSafe()
        val errorMessage = "You need to add: ${this.getBaseDefinition()}"
        Assertions.assertNotNull(clazz, errorMessage)
        Assertions.assertTrue(
            clazz!!.isSameWith(this),
            "$errorMessage, but currently you added: ${
                clazz.toTestClass(this.name, this.classPackage).getBaseDefinition()
            }"
        )
        if (isDataClass) {
            clazz.checkIfIsDataClass(this)
        }
        if (this.interfaces.isNotEmpty()) {
            checkInterfaces(clazz)
        }
        return clazz
    }

    private fun checkInterfaces(clazz: Class<*>) {
        val clazzInterfaces = clazz.interfaces
        Assertions.assertEquals(
            this.interfaces.size,
            clazzInterfaces.size,
            "The class ${getFullName()} must have ${this.interfaces.size} direct superclasses"
        )
        this.interfaces.forEach {
            val currentClazz = it.findClass()
            Assertions.assertTrue(
                currentClazz in clazzInterfaces,
                "The class ${getFullName()} must have ${it.getFullName()} as a direct superclass"
            )
        }
    }

    private fun checkFields(clazz: Class<*>) = checkVariables(clazz, this.declaredFields)

    private fun checkVariables(clazz: Class<*>, variables: List<TestVariable>, toCheckMutability: Boolean = true) {
        val declaredFields = clazz.getDeclaredFieldsWithoutCompanion()
        variables.forEach { field ->
            val currentField = declaredFields.find { it.name == field.name }
            Assertions.assertNotNull(currentField, "Can not find the field with name ${field.name}")
            field.checkField(currentField!!, toCheckMutability)
        }
    }

    fun checkEnumEntryDefinition(clazz: Class<*>) = checkVariables(clazz, this.declaredEnumEntries, false)

    fun checkFieldsDefinition(clazz: Class<*>, toCheckDeclaredFieldsSize: Boolean = true) {
        if (toCheckDeclaredFieldsSize) {
            Assertions.assertEquals(
                clazz.getDeclaredFieldsWithoutCompanion().size,
                this.declaredFields.size,
                "You need to declare the following fields: ${this.getFieldsListPrettyString()}"
            )
        }
        this.checkFields(clazz)
    }

    fun getJavaClass(): Class<*> {
        val clazz = this.findClassSafe()
        Assertions.assertNotNull(clazz, "You need to add: ${this.getBaseDefinition()}")
        return clazz!!
    }

    fun checkNoConstructors(clazz: Class<*>) {
        Assertions.assertTrue(clazz.constructors.isEmpty(), "The ${getBaseDefinition()} must not have any constructors")
    }

    fun getObjectInstance(clazz: Class<*>): Any {
        val field = clazz.getInstanceFiled()
        Assertions.assertNotNull(field, "Did not find the INSTANCE of the ${getFullName()}")
        return field!!.get(clazz) ?: error("Did not get the INSTANCE of the ${getFullName()}")
    }

    fun checkConstructors(clazz: Class<*>, constructorGetters: List<ConstructorGetter>): Constructor<out Any> {
        Assertions.assertTrue(constructorGetters.isNotEmpty())
        val arguments = constructorGetters.map { it.parameterTypes }.toSet()
        val constructors = mutableListOf<Constructor<*>>()
        constructorGetters.forEach {
            it.getConstructorWithDefaultArguments(clazz)?.let { constructor ->
                constructors.add(constructor)
            }
        }

        Assertions.assertTrue(
            constructors.isNotEmpty(),
            """
                You don't have any constructors with ${arguments.first().size} arguments in the class $name. 
                Please, check the arguments, probably you need to add the default values.
            """
        )
        return constructors.first()
    }

    fun checkDeclaredMethods(clazz: Class<*>) = customMethods.forEach { checkMethod(clazz, it) }

    fun checkMethod(clazz: Class<*>, method: TestMethod) {
        val candidates = if (method.visibility == Visibility.PRIVATE) {
            clazz.declaredMethods // private methods
        } else {
            clazz.methods // other methods
        }

        method.checkMethod(candidates.findMethod(method))
    }

    fun findMethod(clazz: Class<*>, method: TestMethod): Method {
        Assertions.assertTrue(
            method in customMethods,
            "The method ${method.name} was not found in the class ${getFullName()}"
        )
        return clazz.methods.findMethod(method)
    }

    fun invokeMethodWithoutArgs(invokeData: TestMethodInvokeData, isPrivate: Boolean = false): Any =
        invokeMethodWithoutArgs(
            clazz = invokeData.clazz,
            instance = invokeData.instance,
            javaMethod = invokeData.method,
            isPrivate = isPrivate,
        )

    fun <T> invokeMethodWithoutArgs(clazz: Class<*>, instance: T, javaMethod: Method, isPrivate: Boolean = false) =
        javaMethod.invokeWithoutArgs(clazz, obj = instance, isPrivate = isPrivate)

    fun invokeMethodWithArgs(vararg args: Any, invokeData: TestMethodInvokeData, isPrivate: Boolean = false): Any =
        invokeMethodWithArgs(
            args = args,
            clazz = invokeData.clazz,
            instance = invokeData.instance,
            javaMethod = invokeData.method,
            isPrivate = isPrivate,
        )

    fun <T> invokeMethodWithArgs(
        vararg args: Any, clazz: Class<*>, instance: T, javaMethod: Method, isPrivate: Boolean = false
    ) = javaMethod.invokeWithArgs(*args, clazz = clazz, obj = instance, isPrivate = isPrivate)
}

fun TestClass.findClass(): Class<*> = Class.forName(this.getFullName())

@Suppress("SwallowedException")
fun TestClass.findClassSafe() = try {
    this.findClass()
} catch (e: ClassNotFoundException) {
    null
}
