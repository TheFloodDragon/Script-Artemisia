package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Expression

class Call(val id : Expr) : Expression {
    override fun visit(parser: Parser): Expr {
        val start = parser.getLocation()
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

        //parser.spilt()

        return Expr.CallExpr(id,states, BigLocation(start,end))
    }
}