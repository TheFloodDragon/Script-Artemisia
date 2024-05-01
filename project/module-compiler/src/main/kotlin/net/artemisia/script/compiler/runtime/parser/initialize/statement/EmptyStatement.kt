package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.location.BigLocation
import compiler.Parser
import compiler.runtime.parser.Statement

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