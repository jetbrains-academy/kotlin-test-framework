package org.jetbrains.academy.test.system.kotlin.test

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.academy.test.system.ij.analyzer.findMethodUsages
import org.jetbrains.academy.test.system.ij.analyzer.hasElementOfTypeWithName
import org.jetbrains.academy.test.system.ij.analyzer.hasExpressionWithParent
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.findMethodsWithContent
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.getMethodCallArguments
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.hasConstantWithGivenValue
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtCallExpression

/**
 * A Base test class to create tests with PSI without adding intellij dependency into courses directly.
 */
open class BaseIjTestClass : BasePlatformTestCase() {

    fun hasConstantWithGivenValue(elementValue: String): Boolean =
        myFixture.file.hasConstantWithGivenValue(elementValue)

    fun findMethodsWithContent(content: String): List<String> =
        myFixture.file.findMethodsWithContent(content)

    fun findMethodUsages(content: String): List<String> =
        myFixture.file.findMethodUsages(content, KtCallExpression::class.java, KtNamedFunction::class.java)

    fun hasProperty(propertyName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(KtProperty::class.java, propertyName)

    fun hasMethod(methodName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(KtNamedFunction::class.java, methodName)

    fun hasClass(className: String): Boolean = myFixture.file.hasElementOfTypeWithName(KtClass::class.java, className)

    fun hasParameter(parameterName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(KtParameter::class.java, parameterName)

    fun hasExpressionWithParent(expression: String, parent: String?, isParentTypeFunction: Boolean = false): Boolean =
        myFixture.file.hasExpressionWithParent(
            expression, parent, isParentTypeFunction, KtNamedFunction::class.java,
            KtDotQualifiedExpression::class.java, KtCallExpression::class.java
        )

    fun getMethodCallArguments(methodName: String): List<String>? =
        myFixture.file.getMethodCallArguments(methodName)
}
