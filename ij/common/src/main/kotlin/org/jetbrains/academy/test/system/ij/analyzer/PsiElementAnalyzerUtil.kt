package org.jetbrains.academy.test.system.ij.analyzer

import com.intellij.psi.PsiElement

/**
 * Retrieves the constant value of the PsiElement.
 *
 * @param constantExpressionClass The classes of the constant expressions to search for.
 * @return The constant value as a string, or null if no constant value is found.
 * @throws IllegalArgumentException If the PsiElement contains multiple constant values.
 */
fun <T : PsiElement> PsiElement.getConstValue(vararg constantExpressionClass: Class<out T>): String? {
    val possibleValue = extractElementsOfTypes(*constantExpressionClass)
    if (possibleValue.isEmpty()) {
        return null
    }
    require(possibleValue.size == 1) { "Parser error! A const variable must have only one value" }
    return possibleValue.first().text.trimIndent()
}

/**
 * Retrieves the body of a block represented by the given code block class.
 *
 * @param codeBlockClass the class representing the code block
 * @return the body of the block as a string, or null if nobody is found
 */
fun <T : PsiElement> PsiElement.getBlockBody(codeBlockClass: Class<T>): String? {
    val possibleBody = extractElementsOfTypes(codeBlockClass)
    if (possibleBody.isEmpty()) {
        return null
    }
    return possibleBody.first().text.trimBraces().trimIndent()
}

/**
 * Trims leading and trailing braces from a string.
 */
private fun String.trimBraces() = dropWhile { it.isWhitespace() }.removePrefix("{")
    .dropLastWhile { it.isWhitespace() }.removeSuffix("}")
