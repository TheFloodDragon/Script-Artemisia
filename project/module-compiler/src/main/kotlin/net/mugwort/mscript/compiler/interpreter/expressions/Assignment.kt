package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.runtime.expection.thrower

class Assignment(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?){
        val assigment = body as Expression.AssignmentExpression
        if (assigment.left is Expression.Identifier){
            env?.set((assigment.left as Expression.Identifier).name,operator(assigment,env))
        }
    }
    private fun operator(operator : Expression.AssignmentExpression,env: Environment?): Any? {
        val value = executor(operator.right,env,interpreter)
        val key = executor(operator.left,env,interpreter)
        val type = getArgumentType(key)
        if (getArgumentType(value) != type){
            thrower.RuntimeException("Error")
        }else{
            if (getArgumentType(value) is Expression.NumericLiteral && type is Expression.NumericLiteral){
                when(operator.operator){
                    "+=" -> {

                        return key.toString().toDouble() + value.toString().toDouble()
                    }
                    "-=" -> {
                        return key.toString().toDouble() - value.toString().toDouble()
                    }
                    "*=" -> {
                        return key.toString().toDouble() * value.toString().toDouble()
                    }
                    "/=" -> {
                        return key.toString().toDouble() / value.toString().toDouble()
                    }
                    "%=" -> {
                        return key.toString().toDouble() % value.toString().toDouble()
                    }
                    else -> {
                        return value
                    }
                }
            }else{
                return value
            }
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
}