package net.mugwort.artemisia.compiler.interpreter.statements.block

import net.mugwort.artemisia.api.Environment
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import net.mugwort.artemisia.core.ast.core.Statement

class BlockStatement(private val interpreter: Interpreter) : StatementExecutor() {
    override val self: StatementExecutor = this

    override fun execute(body: Statement, env: Environment?) {
        val block = body as Statement.BlockStatement
        for (i in block.body) {
            if (i is Statement.ReturnStatement) {
                ReturnStatement().execute(i, null)
                return
            }
            executor(i, env,interpreter)
        }
    }
}