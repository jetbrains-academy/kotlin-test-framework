package org.jetbrains.academy.test.system.ij.analyzer

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentsOfType

/** Extracts elements of given type from related files in project. */
fun <T : PsiElement> PsiElement.extractElementsOfTypes(vararg psiElementClass: Class<out T>): MutableCollection<T> =
    psiElementClass.flatMap { PsiTreeUtil.collectElementsOfType(this, it) }.toMutableList()

/**
 * Checks if the PsiFile contains an element of the specified type and with the given name.
 *
 * @param T the type of the PsiNamedElement subclass to check for
 * @param psiElementClass the class object representing the type of the element to check for
 * @param name the name of the element to search for
 * @return true if an element of the specified type and name exists in the PsiFile, false otherwise
 */
fun <T : PsiNamedElement> PsiFile.hasElementOfTypeWithName(psiElementClass: Class<out T>, name: String): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        extractElementsOfTypes(psiElementClass).any { it.name == name }
    }

/**
 * Retrieves the text of the parent element of the given element.
 *
 * @param element The element for which to retrieve the parent text.
 * @param isParentTypeFunction True if the parent element's type is a function; false otherwise.
 * @param parentClass The class of the parent element.
 * @return The text of the parent element, or null if the parent does not exist or is not of the specified type.
 */
private fun <T : PsiNamedElement> getParentText(
    element: PsiElement,
    isParentTypeFunction: Boolean,
    parentClass: Class<out T>
): String? {
    return if (isParentTypeFunction) {
        element.parentsOfType(parentClass).firstOrNull()?.name
    } else {
        element.parent?.text
    }
}

/**
 * Checks if the given PsiFile has an expression with the specified parent.
 *
 * @param expression The text of the expression to search for.
 * @param parent The text of the parent to compare with.
 * @param isParentTypeFunction Specifies if the parent should be treated as a function or not.
 * @param parentClass The class of the parent element.
 * @param expressionClass The classes of the expression elements to search for.
 * @return true if the PsiFile has an expression with the specified parent, false otherwise.
 */
fun <T : PsiElement, V : PsiNamedElement> PsiFile.hasExpressionWithParent(
    expression: String,
    parent: String?,
    isParentTypeFunction: Boolean,
    parentClass: Class<out V>,
    vararg expressionClass: Class<out T>
): Boolean =
    ApplicationManager.getApplication().runReadAction<Boolean> {
        val expressions: MutableCollection<PsiElement> = extractElementsOfTypes(*expressionClass)
        expressions.any { it.text == expression && getParentText(it, isParentTypeFunction, parentClass) == parent }
    }

/**
 * Finds usages of a given method in the PSI file.
 *
 * @param methodName The name of the method to find usages of.
 * @param callExpressionClass The class representing the call expression to search for.
 * @param methodClass The class representing the method to search for usages of.
 * @return A list of strings representing the names of the methods in which the given method is used.
 */
fun <T : PsiElement, V : PsiNamedElement> PsiFile.findMethodUsages(
    methodName: String,
    callExpressionClass: Class<T>,
    methodClass: Class<V>
): List<String> =
    ApplicationManager.getApplication().runReadAction<List<String>> {
        val callExpression = extractElementsOfTypes(callExpressionClass)
        callExpression.filter { it.text == methodName }.mapNotNull {
            it.parentsOfType(methodClass).firstOrNull()?.name
        }.toList()
    }
