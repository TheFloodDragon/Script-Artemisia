package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Identifier

class AnnotationDeclaration : Statement {
    override fun visit(parser: Parser): State.AnnotationDeclaration {
        val start = parser.getLocation()
        parser.consume(TokenType.ANNOTATION)
        val id = Identifier().visit(parser)

        val params = ArrayList<State>()
        if (parser.match(TokenType.LEFT_PAREN)){
            parser.consume(TokenType.LEFT_PAREN)
            while (true){
                if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)
                if (parser.match(TokenType.RIGHT_PAREN)){
                    parser.consume(TokenType.RIGHT_PAREN)
                    break
                }
                params.add(when(parser.look().type){
                    TokenType.AT -> {
                        AnnotationStatement().visit(parser)
                    }
                    else -> {
                        VariableStatement().visit(parser)
                    }
                })
            }
        }

        val block = if (parser.match(TokenType.LEFT_BRACE)){
            BlockStatement().visit(parser)
        }else null
        val end = parser.getLocation()
        return State.AnnotationDeclaration(id,params,block, BigLocation(start, end))
    }
}