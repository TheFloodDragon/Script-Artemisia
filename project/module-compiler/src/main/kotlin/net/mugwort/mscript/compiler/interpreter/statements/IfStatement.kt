package net.mugwort.mscript.compiler.interpreter.statements

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.mscript.core.ast.core.Statement

class IfStatement(private val interpreter: Interpreter) : StatementExecutor() {
    override val self: StatementExecutor
        get() = this

    override fun execute(body: Statement, env: Environment?) {
        val state = body as Statement.IfStatement
        val rule = ExpressionExecutor.executor(body.rule,env,interpreter) as Boolean
        println(rule)
        if (rule){
            executor(body.consequent, env, interpreter)
        }else{
            if (state.alternate != null){
                executor(body.alternate!!, env, interpreter)
            }
        }
    }
}
