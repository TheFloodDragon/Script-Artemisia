package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.expection.thrower
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Literal : Expression{

    override fun visit(parser: Parser): Expr {
        fun BooleanLiteral(): Expr.BooleanLiteral {
            return Expr.BooleanLiteral(parser.consume(if (parser.look().type == TokenType.TRUE) TokenType.TRUE else TokenType.FALSE)!!.value.toBoolean())
        }

        fun NumericLiteral(): Expr.NumericLiteral {
            return Expr.NumericLiteral(parser.consume(TokenType.NUMBER)!!.value.toDouble())
        }

        fun StringLiteral(): Expr.StringLiteral {

            return Expr.StringLiteral(parser.consume(TokenType.STRING)!!.value)
        }



        fun NullLiteral(): Expr.NullLiteral {
            parser.consume(TokenType.NULL)
            return Expr.NullLiteral
        }



        return when (parser.look().type) {
            TokenType.NUMBER -> NumericLiteral()
            TokenType.STRING -> StringLiteral()
            TokenType.FALSE, TokenType.TRUE -> BooleanLiteral()
            TokenType.NULL -> NullLiteral()
            else -> {
                thrower.send(
                    "Unexpected literal production ['${parser.look().type}']",
                    "LiteralError",
                    parser.file,
                    BigLocation(parser.look().location,parser.look().location)
                )

                Expr.NullLiteral
            }
        }
    }
}