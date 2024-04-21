package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

class ForStatement : Statement {
    override fun visit(parser: Parser): State {
        val s = parser.getLocation()
        parser.consume(TokenType.FOR)
        if (parser.match(TokenType.LEFT_PAREN))parser.consume(TokenType.LEFT_PAREN)
        val rule = parser.getExpr()
        if (parser.match(TokenType.RIGHT_PAREN))parser.consume(TokenType.RIGHT_PAREN)
        val body = parser.getState()
        val end = parser.getLocation()
        return State.ForState(rule, body, BigLocation(s, end))
    }
}