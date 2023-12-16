package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class CallExpression : Expressions() {
    override val self: Expressions = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val expr = body as Expression.CallExpression
        val params = arrayListOf<Any?>()
        for (param in expr.arguments) {
            if (env != null) {
                params.add(executor(param, env))
            } else {
                params.add(executor(param,null))
            }
        }
        return runner(expr.caller.name, params, env)
    }
}