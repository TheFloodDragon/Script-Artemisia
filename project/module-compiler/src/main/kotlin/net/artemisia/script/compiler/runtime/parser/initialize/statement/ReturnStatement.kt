package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

class ReturnStatement : Statement {
    override fun visit(parser: Parser): State {
        val start = parser.getLocation()
        parser.consume(TokenType.RETURN)
        val state = parser.getState()
        val end = parser.getLocation()
        return State.ReturnState(state,BigLocation(start,end))
    }
}