package org.jetbrains.academy.test.system.models

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KVisibility

/**
 * Represents visibility of variables, methods, classes. etc.
 */
enum class Visibility(val key: String) {
    PUBLIC("public"),
    PRIVATE("private"),
    ;
}


@Suppress("SwallowedException")
fun KVisibility.asVisibility() = try {
    Visibility.valueOf(name)
} catch (e: IllegalArgumentException) {
    null
}

fun Field.getVisibility() = this.modifiers.getVisibility()

fun Int.getVisibility(): Visibility? {
    if (Modifier.isPublic(this)) {
        return Visibility.PUBLIC
    }
    if (Modifier.isPrivate(this)) {
        return Visibility.PRIVATE
    }
    return null
}
