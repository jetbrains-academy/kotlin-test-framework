package org.jetbrains.academy.test.system

import org.jetbrains.academy.test.system.models.Visibility
import org.jetbrains.academy.test.system.models.classes.ClassType
import org.jetbrains.academy.test.system.models.classes.TestClass
import org.jetbrains.academy.test.system.models.getVisibility
import java.lang.reflect.Modifier
import kotlin.jvm.internal.DefaultConstructorMarker

private fun Class<*>.getVisibility() = this.modifiers.getVisibility()

fun Class<*>.getDeclaredFieldsWithoutCompanion() =
    this.declaredFields.filter { it.name !in listOf("Companion", "INSTANCE") }

@Suppress("ReturnCount", "ForbiddenComment")
private fun Class<*>.getClassType(): ClassType {
    if (this.isInterface) {
        if (this.isSamInterface()) {
            return ClassType.SAM_INTERFACE
        }
        return ClassType.INTERFACE
    }
    if (this.isEnum) {
        return ClassType.ENUM
    }
    if (this.isObject()) {
        return ClassType.OBJECT
    }
    // TODO: think about companion object
    return ClassType.CLASS
}

fun Class<*>.isSamInterface(): Boolean {
    if (methods.size != 1) {
        return false
    }
    return this.kotlin.isFun
}

fun Class<*>.getInstanceFiled() = this.fields.find { it.name == "INSTANCE" }

fun Class<*>.isObject() = this.fields.all { Modifier.isStatic(it.modifiers) } && this.getInstanceFiled() != null

fun Class<*>.checkIfIsDataClass(testClass: TestClass) {
    val methods = this.methods
    val methodsNames = methods.map { it.name }
    val dataClassMethods = listOf(
        "copy",
        "equals",
        "hashCode",
        "toString",
    )
    dataClassMethods.forEach { dataClassMethod ->
        assert(dataClassMethod in methodsNames || methodsNames.any { dataClassMethod in it }) { "${testClass.getFullName()} must be a data class" }
    }
    val componentN = testClass.declaredFields.filter { it.isInPrimaryConstructor && it.visibility == Visibility.PUBLIC }
    val componentNFunctions = methodsNames.filter { "component" in it }
    val componentNErrorMessage =
        "You must put only ${componentN.size} public fields into the primary constructor: ${componentN.joinToString(", ") { it.name }}."
    assert(componentNFunctions.size == componentN.size) { componentNErrorMessage }
    componentN.forEachIndexed { index, _ ->
        val name = "component${index + 1}"
        assert(name in methodsNames || methodsNames.any { name in it }) { componentNErrorMessage }
    }
    val primary = testClass.declaredFields.filter { it.isInPrimaryConstructor }
    val constructorErrorMessage =
        "You must put only ${primary.size} fields into the primary constructor: ${primary.joinToString(", ") { it.name }}."
    require(this.constructors.isNotEmpty()) { "The data class must have at least one constructor!" }
    assert(this.constructors.any { constructor ->
        constructor.parameterTypes.filter { it != DefaultConstructorMarker::class.java }.size == primary.size
    }) { constructorErrorMessage }
}

private fun Class<*>.hasSameVisibilityWith(testClass: TestClass) = this.getVisibility() == testClass.visibility

private fun Class<*>.hasSameClassTypeWith(testClass: TestClass) = this.getClassType() == testClass.classType

fun Class<*>.isSameWith(testClass: TestClass) =
    this.hasSameVisibilityWith(testClass) && this.hasSameClassTypeWith(testClass)

fun Class<*>.toTestClass(name: String, classPackage: String?): TestClass {
    val visibility = this.getVisibility() ?: throwInternalLibError()
    return TestClass(name, classPackage, visibility, this.getClassType())
}
