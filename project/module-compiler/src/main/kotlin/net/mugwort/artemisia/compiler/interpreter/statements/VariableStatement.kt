package net.mugwort.artemisia.compiler.interpreter.statements

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.core.ast.token.BigLocation
import net.mugwort.artemisia.core.ast.token.Location

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
                    it, BigLocation(Location(1,1), Location(1,1))
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