package net.mugwort.mscript.compiler.interpreter.statements

import net.mugwort.mscript.compiler.interpreter.statements.block.BlockStatement
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

abstract class Statements {
    abstract val self: Statements
    fun executor(body: Statement,env: Environment?){
        when(body){
            is Statement.BlockStatement -> BlockStatement().execute(body,env)
            else ->{

            }
        }
    }

    abstract fun execute(body: Statement, env: Environment?) : Any?
}