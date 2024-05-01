package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement

class ReturnStatement : Statement {
    override fun visit(parser: Parser): State {
        val start = parser.getLocation()
        parser.consume(TokenType.RETURN)
        val state = parser.getState()
        val end = parser.getLocation()
        return State.ReturnState(state,BigLocation(start,end))
    }
}