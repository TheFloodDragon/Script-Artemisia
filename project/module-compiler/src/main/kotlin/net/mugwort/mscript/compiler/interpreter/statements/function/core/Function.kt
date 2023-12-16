package net.mugwort.mscript.compiler.interpreter.statements.function.core

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.mscript.compiler.interpreter.statements.block.BlockStatement
import net.mugwort.mscript.compiler.interpreter.statements.block.ReturnStatement
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower

class Function(private val declaration: Statement.FunctionDeclaration, private val parent: Environment, private val interpreter : Interpreter) :
    ICallable {
    override val paramCount: Int = declaration.params.count()
    override fun call(arguments: List<Any?>): Any? {
        val env = Environment(parent)
        val size = if (paramCount >= 0) paramCount else arguments.size

        for (i in 0 until size) {
            if (arguments.size < size){
                thrower.RuntimeException("Error! lost some params")
            }
            if (getArgumentType(arguments[i]) != declaration.params[i].declarations.init) thrower.RuntimeException("Type not equals")
            env.define(declaration.params[i].declarations.id.name, arguments[i])
        }
        try {
            BlockStatement(interpreter).execute(declaration.body,env)
        }catch (e : ReturnStatement.ReturnException){
            return ExpressionExecutor.executor(e.expression,env,interpreter)
        }
        return null
    }
    private fun getArgumentType(argument: Any?) : Expression {
        return when(argument){
            is Number -> Expression.NumericLiteral(null)
            is String -> Expression.StringLiteral(null)
            is Boolean -> Expression.BooleanLiteral(null)
            is Expression.Identifier -> Expression.Identifier(argument.name)
            else -> Expression.ObjectLiteral(null)
        }
    }

    override fun toString(): String {
        return "Function"
    }
}