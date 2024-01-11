package net.mugwort.artemisia.compiler.interpreter.statements

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.artemisia.core.ast.core.Statement

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
