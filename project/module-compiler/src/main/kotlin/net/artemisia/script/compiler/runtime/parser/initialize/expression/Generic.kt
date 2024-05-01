package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Expression

class Generic : Expression {
    override fun visit(parser: Parser): Expr {
        val id = Identifier().visit(parser)
        parser.consume(TokenType.LESS)
        val generic : ArrayList<Expr> = arrayListOf()
        while (!parser.match(TokenType.ARROW)){
            if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)
            generic.add(parser.getExpr())
        }
        parser.consume(TokenType.ARROW)
        return Expr.GenericExpr(id, generic)
    }
}