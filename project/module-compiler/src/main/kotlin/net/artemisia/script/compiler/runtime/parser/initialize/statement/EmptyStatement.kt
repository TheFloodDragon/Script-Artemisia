package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

class EmptyStatement : Statement {
    /**
     *  Empty Statement
     *
     * <; | \n>
     * */
    override fun visit(parser: Parser): State {
        val start = parser.getLocation()
        parser.spilt()
        val end = parser.getLocation()
        return State.EmptyState(BigLocation(start, end))
    }
}