package net.mugwort.mscript.compiler.interpreter.statements.block

import net.mugwort.mscript.compiler.interpreter.statements.Statements
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

class BlockStatement : Statements() {
    override val self: Statements = this

    override fun execute(body: Statement, env: Environment?) {
        val block = body as Statement.BlockStatement
        for (i in block.body) {
            if (i is Statement.ReturnStatement) {
                ReturnStatement().execute(body,null)
            }
            executor(body, env)
        }
    }
}