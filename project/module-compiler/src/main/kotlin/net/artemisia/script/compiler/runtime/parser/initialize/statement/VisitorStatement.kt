package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.location.BigLocation
import compiler.Parser
import compiler.runtime.parser.Statement

class VisitorStatement : Statement {
    override fun visit(parser: Parser): State {
        val s = parser.getLocation()
        val name = parser.consume(parser.look().type)!!.value
        val state = parser.getState()
        val e = parser.getLocation()
        return State.VisitorState(name,state, BigLocation(s,e))
    }
}