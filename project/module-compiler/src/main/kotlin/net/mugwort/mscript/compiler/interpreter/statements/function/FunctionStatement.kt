package net.mugwort.mscript.compiler.interpreter.statements.function

import net.mugwort.mscript.compiler.interpreter.statements.Statements
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class FunctionStatement : Statements() {
    override val self: Statements = this
    override fun execute(body: Statement, env: Environment?){

    }

}