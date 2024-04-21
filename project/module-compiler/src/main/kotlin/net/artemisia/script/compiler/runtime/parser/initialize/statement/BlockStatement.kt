package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

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