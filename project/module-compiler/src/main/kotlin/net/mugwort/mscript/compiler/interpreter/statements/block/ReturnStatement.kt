package net.mugwort.mscript.compiler.interpreter.statements.block

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.statements.Statements
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class ReturnStatement : Statements() {
    override val self: Statements = this
    override fun execute(body: Statement, env: Environment?) {
        val statement = body as Statement.ReturnStatement
        if (statement.argument != null){
            throw ReturnException(statement.argument!!, "return")
        }else{
            throw Interpreter.ReturnException(Expression.NullLiteral, "return")
        }
    }
    inner class ReturnException(val expression: Expression, message: String) : Exception(message)
}