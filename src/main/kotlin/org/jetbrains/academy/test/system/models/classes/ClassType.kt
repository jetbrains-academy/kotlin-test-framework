package org.jetbrains.academy.test.system.models.classes

/**
 * Represents all possible class types which are supported by this test framework.
 */
enum class ClassType(val key: String) {
    CLASS("class"),
    INTERFACE("interface"),
    SAM_INTERFACE("fun interface"),
    ENUM("enum class"),
    OBJECT("object"),
//    COMPANION_OBJECT
    ;
}