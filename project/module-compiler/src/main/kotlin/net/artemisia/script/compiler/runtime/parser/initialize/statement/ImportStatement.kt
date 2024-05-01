package compiler.runtime.parser.initialize.statement

import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement

class ImportStatement : Statement {
    override fun visit(parser: Parser): State.ImportState {
        val start = parser.getLocation()
        parser.consume(TokenType.IMPORT)
        if (parser.match(TokenType.IDENTIFIER)){
            val file = parser.getExpr()
            val end = parser.getLocation()
            parser.spilt()
            return State.ImportState(file, arrayListOf(),true, BigLocation(start, end))
        }else{
            var all = false
            val objs : ArrayList<Expr> = arrayListOf()
            parser.consume(TokenType.LEFT_BRACE)
            while (!parser.match(TokenType.RIGHT_BRACE)){
                if (parser.match(TokenType.COMMA)){
                    parser.consume(TokenType.COMMA)
                }else if (parser.match(TokenType.STAR)){
                    parser.consume(TokenType.STAR)
                    all = true
                    while (!parser.match(TokenType.RIGHT_BRACE)) parser.advance()
                    break
                }
                objs.add(parser.getExpr())

            }
            parser.consume(TokenType.RIGHT_BRACE)
            parser.consume(TokenType.IN)
            val file = parser.getExpr()
            val end = parser.getLocation()
            parser.spilt()
            return State.ImportState(file, objs,all, BigLocation(start, end))
        }
    }
}