package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Expression

class Identifier : Expression{
    override fun visit(parser: Parser): Expr.Identifier {
        return Expr.Identifier(parser.consume(TokenType.IDENTIFIER)!!.value)
    }
}