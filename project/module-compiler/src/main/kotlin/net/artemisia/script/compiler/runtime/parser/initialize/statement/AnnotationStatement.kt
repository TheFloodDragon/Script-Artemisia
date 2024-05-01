package compiler.runtime.parser.initialize.statement

import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Identifier


class AnnotationStatement : Statement {
    override fun visit(parser: Parser): State {
        val s = parser.getLocation()
        parser.consume(TokenType.AT)
        val id = Identifier().visit(parser)
        if (parser.match(TokenType.LEFT_PAREN)){
            parser.consume(TokenType.LEFT_PAREN)
            val array = arrayListOf<Expr>()
                while (parser.look().type != TokenType.RIGHT_BRACE){
                    if (parser.match(TokenType.RIGHT_PAREN)) {
                        parser.consume(TokenType.RIGHT_PAREN)
                        break
                    }
                    if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)
                    array.add(parser.getExpr())
                }
            val e = parser.getLocation()
            return State.AnnotationState(id,array, BigLocation(s,e))
        }
        val e = parser.getLocation()
        return State.AnnotationState(id,null, BigLocation(s,e))
    }
}