package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.runtime.Math
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.Environment

class Binary(private val interpreter: Interpreter?) : ExpressionExecutor(){
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val expr = body as Expression.BinaryExpression
        var left = when (expr.left) {
            is Expression.NumericLiteral -> {
                (expr.left as Expression.NumericLiteral).value!!
            }

            is Expression.StringLiteral -> {
                (expr.left as Expression.StringLiteral).value!!
            }

            else -> {
                executor(expr.left,env,interpreter)
            }
        }
        val operator = expr.operator
        var right = when (expr.right) {
            is Expression.NumericLiteral -> {
                (expr.right as Expression.NumericLiteral).value!!
            }

            is Expression.StringLiteral -> {
                (expr.right as Expression.StringLiteral).value!!
            }

            else -> {
                executor(expr.right,env,interpreter)

            }
        }
        if (left is Expression.GroupExpression) {
            left = Group(interpreter).visit(left, env)
        }
        if (right is Expression.GroupExpression) {
            right = Group(interpreter).visit(right, env)
        }


        return Math(left, operator, right)
    }

}