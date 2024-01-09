package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.expection.thrower

class Logical(private val interpreter: Interpreter?) : ExpressionExecutor(){
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val logical = body as Expression.LogicalExpression
        val operator = logical.operator

        val left = executor(logical.left,env,interpreter)
        val right = executor(logical.right,env, interpreter)

        return if (left is Number && right is Number){
            when(operator){
                "<=" -> return left.toDouble() <= right.toDouble()
                ">=" -> return left.toDouble() >= right.toDouble()
                "<" -> return left.toDouble() < right.toDouble()
                ">" -> return left.toDouble() > right.toDouble()
                "==" -> return left.toDouble() == right.toDouble()
                "!=" -> return left.toDouble() != right.toDouble()
                else -> {}
            }
        }else if (left is Boolean && right is Boolean){
            when(operator){
                "||" -> return left || right
                "&&" -> return left && right
                "==" -> return left == right
                "!=" -> return left != right
                else -> {}
            }
        }else{
            return when(operator){
                "==" -> return left == right
                "!=" -> return left != right
                else ->{

                    thrower.RuntimeException("The Type can`t judgment")
                }
            }

        }
    }
}