package org.jetbrains.academy.test.system.test

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.academy.test.system.ij.analyzer.findMethodsWithContent
import org.jetbrains.academy.test.system.ij.analyzer.hasConstantWithGivenValue


/**
 * A Base test class to create tests with PSI without adding intellij dependency into courses directly.
 */
open class BaseIjTestClass : BasePlatformTestCase() {

    fun hasConstantWithGivenValue(elementValue: String): Boolean =
        myFixture.file.hasConstantWithGivenValue(elementValue)

    fun findMethodsWithContent(content: String): List<String> =
        myFixture.file.findMethodsWithContent(content)
}
