package org.jetbrains.academy.test.system.java.test

import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiNewExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiCallExpression
import com.intellij.psi.PsiLocalVariable
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.academy.test.system.ij.analyzer.findMethodUsages
import org.jetbrains.academy.test.system.ij.analyzer.hasElementOfTypeWithName
import org.jetbrains.academy.test.system.ij.analyzer.hasExpressionWithParent
import org.jetbrains.academy.test.system.java.ij.analyzer.findMethodsWithContent
import org.jetbrains.academy.test.system.java.ij.analyzer.hasConstantWithGivenValue

/**
 * A Base test class to create tests with PSI without adding intellij dependency into courses directly.
 */
open class BaseIjTestClass : BasePlatformTestCase() {

    fun hasConstantWithGivenValue(elementValue: String): Boolean =
        myFixture.file.hasConstantWithGivenValue(elementValue)

    fun findMethodsWithContent(content: String): List<String> =
        myFixture.file.findMethodsWithContent(content)

    fun findMethodUsages(content: String): List<String> =
        myFixture.file.findMethodUsages(content, PsiCallExpression::class.java, PsiMethod::class.java)

    fun hasField(fieldName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(PsiField::class.java, fieldName)

    fun hasLocalVariable(localVariableName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(PsiLocalVariable::class.java, localVariableName)

    fun hasMethod(methodName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(PsiMethod::class.java, methodName)

    fun hasClass(className: String): Boolean = myFixture.file.hasElementOfTypeWithName(PsiClass::class.java, className)

    fun hasParameter(parameterName: String): Boolean =
        myFixture.file.hasElementOfTypeWithName(PsiParameter::class.java, parameterName)

    fun hasExpressionWithParent(expression: String, parent: String?, isParentTypeFunction: Boolean = false): Boolean =
        myFixture.file.hasExpressionWithParent(
            expression, parent, isParentTypeFunction, PsiMethod::class.java,
            PsiNewExpression::class.java, PsiReferenceExpression::class.java, PsiMethodCallExpression::class.java
        )
}
