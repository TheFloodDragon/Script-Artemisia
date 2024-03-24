package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Generic : Expression {
    override fun visit(parser: Parser): Expr {
        val id = Identifier().visit(parser)
        parser.consume(TokenType.LESS)
        val generics : ArrayList<Expr> = arrayListOf()
        while (parser.look().type != TokenType.ARROW){
            if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)

        }
        parser.consume(TokenType.ARROW)
        return Expr.GenericExpr(id, generics)
    }
}