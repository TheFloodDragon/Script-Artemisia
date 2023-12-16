package net.mugwort.mscript.compiler.interpreter.expressions.runtime

import net.mugwort.mscript.core.ast.core.Expression

class Literal(private val expr: Expression?) {
    fun get(): Any? {
        return when (expr) {
            is Expression.Identifier -> {
                expr.name
            }

            is Expression.ObjectLiteral -> {
                expr.value
            }

            is Expression.NullLiteral -> {
                null
            }

            is Expression.NumericLiteral -> {
                expr.value
            }

            is Expression.StringLiteral -> {
                expr.value
            }

            is Expression.BooleanLiteral -> {
                expr.value
            }

            else -> {
                null
            }
        }
    }
}