package net.mugwort.mscript.compiler.interpreter.statements.function

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import net.mugwort.mscript.core.ast.core.Statement

class FunctionStatement(private val interpreter: Interpreter) : StatementExecutor() {
    override val self: StatementExecutor = this
    override fun execute(body: Statement, env: Environment?){
        newFunction((body as Statement.FunctionDeclaration),env)

    }
    fun newFunction(state: Statement.FunctionDeclaration, env: Environment?){
        env?.addFunction(state.identifier.name, Function(state, env, interpreter))
    }

}