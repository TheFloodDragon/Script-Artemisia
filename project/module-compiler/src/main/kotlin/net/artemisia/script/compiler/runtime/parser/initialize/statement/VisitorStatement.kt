package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

class VisitorStatement : Statement {
    override fun visit(parser: Parser): State {
        val s = parser.getLocation()
        val name = parser.consume(parser.look().type)!!.value
        val state = parser.getState()
        val e = parser.getLocation()
        return State.VisitorState(name,state, BigLocation(s,e))
    }
}