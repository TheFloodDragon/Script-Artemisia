package compiler.runtime.parser.initialize.statement

import common.ast.State
import common.expection.thrower
import common.location.BigLocation
import common.location.Location
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Identifier

class VariableStatement(private val final : Boolean = false) : Statement {
    override fun visit(parser: Parser): State.VariableDeclaration {
        val start = parser.getLocation()
        if (final) parser.consume(TokenType.FINAL)
        if (parser.match(TokenType.LET)) parser.consume(TokenType.LET)
        val id = Identifier().visit(parser)
        return if (parser.match(TokenType.COLON)){
            parser.consume(TokenType.COLON)
            val type = parser.getExpr()
            val init = getInit(start,parser)
            val end = parser.getLocation()
            State.VariableDeclaration(id,init,type,final, BigLocation(start, end))
        }else if(parser.match(TokenType.EQUAL)){
            val init = getInit(start,parser)
            val end = parser.getLocation()
            State.VariableDeclaration(id,init,null,final, BigLocation(start, end))
        }else{
            val end = parser.getLocation()
            thrower.send("Non-Type of This Variable","NotType",parser.file, BigLocation(start, end),true)
            State.VariableDeclaration(id,null,null,final, BigLocation(start, end))
        }
    }
    private fun getInit(start: Location, parser: Parser): State? {
        if (final) {
            return final(start,parser)
        }
        if (parser.match(TokenType.EQUAL)) {
            parser.consume(TokenType.EQUAL)
            return parser.getState()
        }
        return null
    }
    private fun final(start : Location, parser : Parser): State {
        if (final && parser.match(TokenType.EQUAL)) {
            parser.consume(TokenType.EQUAL)
            return parser.getState()
        }else{
            val end = parser.getLocation()
            thrower.send("constant must be init","NotInit",parser.file, BigLocation(start, end),true)
            return parser.getState()
        }
    }
}