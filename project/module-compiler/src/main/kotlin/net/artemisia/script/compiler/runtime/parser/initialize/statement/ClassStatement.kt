package compiler.runtime.parser.initialize.statement

import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import common.location.Location
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Call
import compiler.runtime.parser.initialize.expression.Identifier
import kotlin.reflect.KClass

class ClassStatement : Statement{


    override fun visit(parser: Parser): State.ClassDeclaration {
        val start = parser.getLocation()
        parser.consume(TokenType.CLASS)
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
        val inherit = ArrayList<Expr>()

        if (parser.match(TokenType.COLON)){
            parser.consume(TokenType.COLON)
            while (!parser.match(TokenType.EOF) || !parser.match(TokenType.LEFT_BRACE) || !parser.match(TokenType.NEWLINE)){
                if (parser.match(TokenType.EOF) || parser.match(TokenType.LEFT_BRACE) || parser.match(TokenType.NEWLINE)) break
                if (parser.check(TokenType.LEFT_PAREN)){
                    inherit.add(Call(Identifier().visit(parser)).visit(parser))
                }else if (parser.match(TokenType.COMMA)){
                  parser.consume(TokenType.COMMA)
                } else{
                    inherit.add(Identifier().visit(parser))
                }
            }
        }
        val block = if (parser.match(TokenType.LEFT_BRACE)){
            BlockStatement().visit(parser)
        }else null
        val end = parser.getLocation()
        return State.ClassDeclaration(id,params,inherit,block, BigLocation(start, end))
    }
}