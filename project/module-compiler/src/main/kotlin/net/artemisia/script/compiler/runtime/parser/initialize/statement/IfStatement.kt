package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement

class IfStatement : Statement{
    override fun visit(parser: Parser): State {
        val s = parser.getLocation()
        parser.consume(TokenType.IF)
        if (parser.match(TokenType.LEFT_PAREN))parser.consume(TokenType.LEFT_PAREN)
        val rule = parser.getExpr()
        if (parser.match(TokenType.RIGHT_PAREN))parser.consume(TokenType.RIGHT_PAREN)
        val body = parser.getState()
        val alt = if (parser.match(TokenType.ELSE)){
            parser.consume(TokenType.ELSE)
            if (parser.match(TokenType.IF)){
                IfStatement().visit(parser)
            }else{
                parser.getState()
            }
        } else null
        val e = parser.getLocation()
        return State.IfState(rule,body,alt,BigLocation(s,e))
    }
}