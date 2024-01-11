package net.mugwort.artemisia.compiler.interpreter.expressions.runtime

import net.mugwort.mscript.compiler.interpreter.expressions.objects.Boolean
import net.mugwort.mscript.compiler.interpreter.expressions.objects.Number
import net.mugwort.mscript.compiler.interpreter.expressions.objects.Object
import net.mugwort.mscript.compiler.interpreter.expressions.objects.String
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.api.Environment

class Literal(private val expr: Expression?) {
    fun get(): Environment? {
        return when (expr) {
            is Expression.Identifier -> {
                Object(expr.name).env
            }

            is Expression.ObjectLiteral -> {
                Object(expr.value).env
            }

            is Expression.NullLiteral -> {
                Object(null).env
            }

            is Expression.NumericLiteral -> {
                expr.value?.let { Number(it).env }
            }

            is Expression.StringLiteral -> {
                expr.value?.let { String(it).env }
            }

            is Expression.BooleanLiteral -> {
                expr.value?.let { Boolean(it).env }
            }

            else -> {
                Environment()
            }
        }
    }
}