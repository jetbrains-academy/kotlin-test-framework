package org.jetbrains.academy.test.system

import org.jetbrains.academy.test.system.models.Visibility
import java.lang.reflect.Modifier
import java.lang.reflect.Type

fun Type.getShortName() = if ("<" in this.toString()) {
    val types = this.toString().split("<").toMutableList()
    types[0].getShortName()
} else {
    this.toString().getShortName()
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
