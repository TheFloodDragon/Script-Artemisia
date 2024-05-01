package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement

class BlockStatement : Statement {
    override fun visit(parser: Parser): State.BlockState {
        val start = parser.getLocation()
        parser.consume(TokenType.LEFT_BRACE)
        val list : ArrayList<State> = arrayListOf()
        while (parser.look().type != TokenType.RIGHT_BRACE){
            list.add(parser.getState())
        }
        parser.consume(TokenType.RIGHT_BRACE)
        val end = parser.getLocation()
        return State.BlockState(list, BigLocation(start, end))
    }
}