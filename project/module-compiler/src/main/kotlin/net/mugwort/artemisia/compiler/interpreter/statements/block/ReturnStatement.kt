package net.mugwort.artemisia.compiler.interpreter.statements.block

import net.mugwort.artemisia.api.Environment
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement

class ReturnStatement : StatementExecutor() {
    override val self: StatementExecutor = this
    override fun execute(body: Statement, env: Environment?) {
        val statement = body as Statement.ReturnStatement
        if (statement.argument != null){
            throw ReturnException(statement.argument!!, "return")
        }else{
            throw ReturnException(Expression.NullLiteral, "return")
        }
    }
    inner class ReturnException(val expression: Expression, message: String) : Exception(message)
}