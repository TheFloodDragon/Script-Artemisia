package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Member(private val obj : Expr? = null) : Expression {
    override fun visit(parser: Parser): Expr {
        val left = obj ?: parser.getExpr()
        if (parser.match(TokenType.LEFT_SQUARE)){
            parser.consume(TokenType.LEFT_SQUARE)
            val right = parser.getExpr()
            parser.consume(TokenType.RIGHT_SQUARE)
            return Expr.MemberExpr(left,right,true)
        }else{
            parser.consume(TokenType.DOT)
            val right = parser.getExpr()
            if (parser.match(TokenType.DOT)){
                return Member(Expr.MemberExpr(left,right,false)).visit(parser)
            }
            return Expr.MemberExpr(left,right,false)
        }
    }
    fun isMember(parser: Parser): Boolean {
        return parser.match(TokenType.LEFT_SQUARE) || parser.match(TokenType.DOT)
    }
}