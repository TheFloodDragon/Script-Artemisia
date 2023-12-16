package net.mugwort.mscript.compiler.interpreter.statements

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class VariableStatement(private val interpreter: Interpreter?) : StatementExecutor() {
    override val self: StatementExecutor
        get() = this

    override fun execute(body: Statement, env: Environment?){
        val statement = body as Statement.VariableStatement
        fun define(environment: Environment) {
            val id = statement.declarations.id.name
            val const = statement.const
            val init = statement.declarations.init?.let {
                Statement.ExpressionStatement(
                    it
                )
            }?.let { ExpressionExecutor.executor(it.expression,env,interpreter) }
            if (const) {
                environment.define(id, init, const)
            } else {
                environment.define(id, init, const)
            }
        }
        env?.let { define(it) }
    }
}