package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.Environment

abstract class Expressions {
    abstract val self: Expressions
    fun executor(body: Expression, env: Environment?){
        when(body){
            else ->{

            }
        }
    }

    abstract fun visit(body: Expression, env: Environment?) : Any?
}