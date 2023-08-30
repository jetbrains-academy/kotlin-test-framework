package org.jetbrains.academy.test.system.kotlin.test

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.findMethodUsages
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.findMethodsWithContent
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.hasConstantWithGivenValue
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.hasMethod
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.hasProperty
import org.jetbrains.academy.test.system.kotlin.ij.analyzer.hasExpressionWithParent


/**
 * A Base test class to create tests with PSI without adding intellij dependency into courses directly.
 */
open class BaseIjTestClass : BasePlatformTestCase() {

    fun hasConstantWithGivenValue(elementValue: String): Boolean =
        myFixture.file.hasConstantWithGivenValue(elementValue)

    fun findMethodsWithContent(content: String): List<String> =
        myFixture.file.findMethodsWithContent(content)

    fun findMethodUsages(content: String): List<String> = myFixture.file.findMethodUsages(content)

    fun hasProperty(propertyName: String): Boolean = myFixture.file.hasProperty(propertyName)

    fun hasMethod(methodName: String): Boolean = myFixture.file.hasMethod(methodName)

    fun hasExpressionWithParent(expression: String, parent: String?, isParentTypeFunction: Boolean = false): Boolean =
        myFixture.file.hasExpressionWithParent(expression, parent, isParentTypeFunction)
}
