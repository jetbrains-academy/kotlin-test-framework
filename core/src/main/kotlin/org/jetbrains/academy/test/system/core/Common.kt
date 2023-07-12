package org.jetbrains.academy.test.system.core

import org.jetbrains.academy.test.system.core.models.Visibility
import java.lang.reflect.Modifier
import java.lang.reflect.Type

fun Type.getShortName(): String {
    val strRepresentation = this.toString().removePrefix("class ")
    return if ("<" in strRepresentation) {
        val types = this.toString().split("<").toMutableList()
        types[0].getShortName()
    } else {
        strRepresentation.getShortName()
    }
}

fun String.getShortName() = lowercase().split(".").last()

fun throwInternalLibError(): Nothing = error("Internal library error!")

@Deprecated(
    "This extensions function is deprecated due to duplication",
    replaceWith = ReplaceWith("getVisibility()", imports = ["org.jetbrains.academy.test.system.models.getVisibility"])
)
fun Int.getVisibility(): Visibility? {
    if (Modifier.isPublic(this)) {
        return Visibility.PUBLIC
    }
    if (Modifier.isPrivate(this)) {
        return Visibility.PRIVATE
    }
    return null
}
