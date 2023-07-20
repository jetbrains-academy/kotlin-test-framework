package org.jetbrains.academy.test.system.test

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty

/**
 * A Base test class to create tests with PSI without adding intellij dependency into courses directly.
 */
open class BaseIjTestClass : BasePlatformTestCase() {

    fun findMethodsWithContent(content: String): List<String> {
        val methodNames = mutableListOf<String>()
        var currentMethod = ""
        ApplicationManager.getApplication().runReadAction<Unit?> {
            myFixture.file.accept(object : PsiRecursiveElementWalkingVisitor() {
                override fun visitElement(element: PsiElement) {
                    when (element) {
                        is KtNamedFunction -> currentMethod = element.name.toString()
                        is KtBlockExpression -> {
                            if (element.text.contains(content)) methodNames.add(currentMethod)
                        }
                    }
                    super.visitElement(element)
                }
            })
        }
        return methodNames
    }

    fun hasConstantWithGivenValue(elementValue: String): Boolean {
        var existsConstant = false
        ApplicationManager.getApplication().runReadAction<Unit?> {
            myFixture.file.accept(object : PsiRecursiveElementWalkingVisitor() {
                override fun visitElement(element: PsiElement) {
                    if (element is KtProperty &&
                        element.modifierList?.text?.contains("const") == true &&
                        element.text.contains(elementValue)
                    )
                        existsConstant = true
                    else super.visitElement(element)
                }
            })
        }
        return existsConstant
    }
}
