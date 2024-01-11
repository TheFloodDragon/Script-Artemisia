package net.mugwort.artemisia.compiler.interpreter.statements.function

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.compiler.interpreter.statements.StatementExecutor
import net.mugwort.artemisia.core.ast.core.Statement

class FunctionStatement(private val interpreter: Interpreter) : StatementExecutor() {
    override val self: StatementExecutor = this
    override fun execute(body: Statement, env: Environment?){
        newFunction((body as Statement.FunctionDeclaration),env)

    }
    fun newFunction(state: Statement.FunctionDeclaration, env: Environment?){
        env?.addFunction(state.identifier.name, Function(state, env, interpreter))
    }

}