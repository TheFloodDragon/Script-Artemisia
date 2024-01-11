package net.mugwort.artemisia.compiler.interpreter.expressions

import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.api.Environment

class Group(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val expr = body as Expression.GroupExpression
        if (expr.expr is Expression.BinaryExpression) {
            return Binary(interpreter).visit(expr.expr, env)
        }
        return null
    }
}