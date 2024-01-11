package net.mugwort.artemisia.compiler.interpreter.expressions

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.api.types.NativeFunction
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.Class
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.NativeClass
import net.mugwort.mscript.compiler.interpreter.statements.function.Function
import net.mugwort.mscript.compiler.interpreter.statements.function.FunctionStatement
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.runtime.expection.thrower

class Call(private val interpreter: Interpreter? = null) : ExpressionExecutor() {
    override val self: ExpressionExecutor = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val expr = body as Expression.CallExpression
        val params = arrayListOf<Any?>()
        for (param in expr.arguments) {
            if (env != null) {
                val arg = executor(param, env,interpreter)
                if (arg is Environment){
                    params.add(arg.get("this"))
                }else{
                    params.add(arg)
                }

            } else {
                val arg = executor(param,null,interpreter)
                if (arg is Environment){
                    params.add(arg.get("this"))
                }else{
                    params.add(arg)
                }
            }
        }

        return caller(expr.caller.name, params, env)
    }

    fun caller(calls: String, params: List<Any?>, env: Environment?): Any? {
        if (interpreter != null){
            for (statement in interpreter.program.body) {
                if (statement is Statement.FunctionDeclaration) {
                    if (statement.identifier.name == calls) {
                        if (env?.get(calls) == null) {
                            FunctionStatement(interpreter).newFunction(statement, env)
                        }
                    }
                }
            }
        }

        val caller = env?.get(calls) ?: interpreter?.globals?.get(calls) ?: thrower.RuntimeException("Cannot Found $calls")
        when (caller) {
            is Class -> {
                return caller.call(params)
            }
            is NativeFunction -> {
                return caller.onCall(params)
            }
            is NativeClass ->{
                return caller.call(params)
            }
            is Function -> {
                return caller.call(params)
            }

            else -> {
                thrower.RuntimeException("The Variable $calls isn`t Function")
            }
        }
        return null
    }
}