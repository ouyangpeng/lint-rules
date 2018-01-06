package com.vanniktech.lintrules.rxjava2

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.WARNING
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet

@Suppress("Detekt.VariableMaxLength") val ISSUE_CALLING_COMPOSITE_DISPOSABLE_DISPOSE = Issue.create("RxJava2DisposableDisposeCall",
    "Marks usage of dispose() on CompositeDisposable.",
    "Instead of using dispose(), clear() should be used. Calling clear will result in a CompositeDisposable that can be used further to add more Disposables. When using dispose() this is not the case.",
    CORRECTNESS, 8, WARNING,
    Implementation(RxJava2CallingDisposableDispose::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

class RxJava2CallingDisposableDispose : Detector(), Detector.UastScanner {
  override fun getApplicableMethodNames() = listOf("dispose")

  override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    if (context.evaluator.isMemberInClass(method, "io.reactivex.disposables.CompositeDisposable")) {
      context.report(ISSUE_CALLING_COMPOSITE_DISPOSABLE_DISPOSE, node, context.getNameLocation(node), "Calling dispose instead of clear.")
    }
  }
}