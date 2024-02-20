package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Call : Expression {
    override fun visit(parser: Parser): Expr {
        val start = parser.getLocation()

        val id = Identifier().visit(parser)

        val states : ArrayList<State> = arrayListOf()
        parser.consume(TokenType.LEFT_PAREN)
        while (parser.look().type != TokenType.RIGHT_PAREN){
            if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)
            states.add(parser.getState())
        }
        parser.consume(TokenType.RIGHT_PAREN)
        val end = parser.getLocation()
        val result = Expr.CallExpr(id,states, BigLocation(start,end))

        val member = Member(result)
        if (Member(result).isMember(parser)) return member.visit(parser)

        parser.spilt()

        return Expr.CallExpr(id,states, BigLocation(start,end))
    }
}