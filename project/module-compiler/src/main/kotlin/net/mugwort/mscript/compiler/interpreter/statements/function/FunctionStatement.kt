package net.mugwort.mscript.compiler.interpreter.statements.function

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class FunctionStatement(private val interpreter: Interpreter? = null) : StatementExecutor() {
    override val self: StatementExecutor = this
    override fun execute(body: Statement, env: Environment?){
        newFunction((body as Statement.FunctionDeclaration),env)
    }
    fun newFunction(state: Statement.FunctionDeclaration, env: Environment?): Unit? {
        return env?.define(state.identifier.name, interpreter?.globals?.let { Function(state, it, interpreter) })
    }

}