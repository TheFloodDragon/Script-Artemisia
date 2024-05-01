package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import common.expection.thrower
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Expression

class Literal : Expression{

    override fun visit(parser: Parser): Expr {
        fun BooleanLiteral(): Expr.BooleanLiteral {
            return Expr.BooleanLiteral(parser.consume(if (parser.look().type == TokenType.TRUE) TokenType.TRUE else TokenType.FALSE)!!.value.toBoolean())
        }

        fun NumericLiteral(): Expr.NumericLiteral {
            val value = parser.consume(TokenType.NUMBER)!!.value
            return if (value.toIntOrNull() != null){
                Expr.NumericLiteral(value.toInt())
            }else if (value.toFloatOrNull() != null)
                Expr.NumericLiteral(value.toFloat())
            else{
                Expr.NumericLiteral(value.toDouble())
            }
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