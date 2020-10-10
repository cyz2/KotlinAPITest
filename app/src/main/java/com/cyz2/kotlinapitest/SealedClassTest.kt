package com.cyz2.kotlinapitest

import android.view.View

class SealedClassTest {
    fun execute(view: View, op: UiOp) = when (op) {
        UiOp.Show -> view.visibility = View.VISIBLE
        UiOp.Hide -> view.visibility = View.GONE
        is UiOp.TranslateX -> view.translationX = op.px // 这个 when 语句分支不仅告诉 view 要水平移动，还告诉 view 需要移动多少距离，这是枚举等 Java 传统思想不容易实现的
        is UiOp.TranslateY -> view.translationY = op.px
        is ViewVis -> view.visibility = op.vis
        NotAOp -> Double.NaN
    }
}
sealed class Expr
data class Const(val number: Double) : Expr()
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()


sealed class UiOp {
    object Show: UiOp()
    object Hide: UiOp()
    class TranslateX(val px: Float): UiOp()
    class TranslateY(val px: Float): UiOp()
}
//其子类可以定在密封类外部，但是必须在同一文件中 v1.1之前只能定义在密封类内部
data class ViewVis(val vis:Int):UiOp()
object NotAOp:UiOp()
