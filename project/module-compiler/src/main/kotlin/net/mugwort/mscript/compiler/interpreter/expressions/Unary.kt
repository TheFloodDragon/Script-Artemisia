package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.expection.thrower

class Unary(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?){
        val unary = body as Expression.UnaryExpression
        env?.set((unary.argument as Expression.Identifier).name,operator(unary,env))
    }
    private fun operator( operator : Expression.UnaryExpression,env: Environment?): Any{
        return if (operator.head){
            when (val literal = executor(operator.argument,env,interpreter)) {
                is Number -> {
                    when(operator.operator){
                        "++" ->  literal.toDouble() + 1
                        "--" -> literal.toDouble() - 1
                        "-" -> -literal.toDouble()
                        else -> {}
                    }
                }

                is Boolean -> {
                    if (operator.operator == "!") return !literal else {
                        thrower.RuntimeException("[\"!\"] just can use of Boolean Type")
                    }
                }

                else -> {

                }
            }
        }else{
            val literal = executor(operator.argument,env,interpreter)
            if (literal is Number){
                when(operator.operator){
                    "++" ->  return literal.toDouble() + 1
                    "--" -> return literal.toDouble() - 1
                    else -> {}
                }
            } else {

            }
        }
    }


}

