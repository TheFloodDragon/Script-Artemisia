package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Identifier : Expression{
    override fun visit(parser: Parser): Expr.Identifier {
        return Expr.Identifier(parser.consume(TokenType.IDENTIFIER)!!.value)
    }
}