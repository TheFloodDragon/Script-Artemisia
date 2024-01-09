package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.api.Environment

class Identifier(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val id = body as Expression.Identifier
        return if (env == null) {
            interpreter?.globals?.get(id.name)


        } else {

            env.get(id.name)
        }
    }
}