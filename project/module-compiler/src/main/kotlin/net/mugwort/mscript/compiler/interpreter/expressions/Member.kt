package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.runtime.Literal
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.Class
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.NativeClass
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.Environment

class Member(private val interpreter: Interpreter?) : ExpressionExecutor() {
    override val self: ExpressionExecutor
        get() = this

    override fun visit(body: Expression, env: Environment?){
        val member = body as Expression.MemberExpression
        if (!member.computed) {
            if(member.objectExpression is Expression.CallExpression){
                val args : ArrayList<Any?> = arrayListOf()
                for (lie in (member.objectExpression as Expression.CallExpression).arguments){
                    args.add(Literal(lie).get())
                }
                val get = Call(interpreter).caller(
                    (member.objectExpression as Expression.CallExpression).caller.name,
                    args,
                    env
                ) as Environment

                executor(member.property,get,interpreter)
            }else{
                val get = executor(member.objectExpression, env,interpreter)
                if (get is NativeClass) {
                    val classEnv = get.call(listOf()) as? Environment
                    executor(member.property, classEnv,interpreter)
                }else if (get is Class){
                    val classEnv = get.call(listOf()) as? Environment
                    executor(member.property, classEnv,interpreter)
                }
            }
        }
    }
}