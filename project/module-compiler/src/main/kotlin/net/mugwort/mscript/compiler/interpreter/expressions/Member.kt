package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.runtime.Literal
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.Class
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.NativeClass
import net.mugwort.mscript.core.ast.core.Expression

class Member(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?): Any? {
        val member = body as Expression.MemberExpression
        return caller(member, env)


    }

    fun caller(member: Expression.MemberExpression, env: Environment?): Any? {
        if (!member.computed) {
            when (val objectExpression = member.objectExpression) {
                is Expression.CallExpression -> {
                    val args: ArrayList<Any?> = arrayListOf()
                    for (lie in objectExpression.arguments) {
                        args.add(Literal(lie).get())
                    }
                    return Call(interpreter).caller(objectExpression.caller.name, args, env)
                }

                is Expression.MemberExpression,is Expression.Identifier  ->{
                    val envs = literal(executor(member.objectExpression,env,interpreter))
                    return executor(member.property,envs,interpreter)
                }

                is Expression.StringLiteral, is Expression.NumericLiteral, is Expression.BooleanLiteral, is Expression.ObjectLiteral -> {
                    val envs = Literal(objectExpression).get()
                    return executor(member.property, envs, interpreter)
                }

                else -> {
                    val get = executor(objectExpression, env, interpreter)
                    when (get) {
                        is NativeClass  -> {
                            val classEnv = get.call(listOf()) as? Environment
                            return executor(member.property, classEnv, interpreter)
                        }
                        is Class -> {
                            val classEnv = get.call(listOf()) as? Environment
                            return executor(member.property, classEnv, interpreter)
                        }

                        is Environment -> {
                            return executor(member.property, get, interpreter)
                        }
                    }
                }
            }
        }
        return null
    }

    fun literal(any: Any?): Environment? {
        return when(any){
            is String -> {
                Literal(Expression.StringLiteral(any)).get()
            }
            is Number -> {
                Literal(Expression.NumericLiteral(any.toDouble())).get()
            }
            is Boolean -> {
                Literal(Expression.BooleanLiteral(any)).get()
            }

            else -> {
                if (any == null)  Literal(Expression.NullLiteral).get()
                Literal(Expression.Identifier(any.toString())).get()
            }
        }
    }

}