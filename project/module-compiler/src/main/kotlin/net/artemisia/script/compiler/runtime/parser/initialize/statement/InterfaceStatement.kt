package compiler.runtime.parser.initialize.statement

import common.ast.Expr
import common.ast.State
import common.expection.thrower
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Call
import compiler.runtime.parser.initialize.expression.Identifier

class InterfaceStatement : Statement{
    override fun visit(parser: Parser): State.InterfaceDeclaration {
        val start = parser.getLocation()
        parser.consume(TokenType.INTERFACE)
        val id = Identifier().visit(parser)

        val params = ArrayList<State>()
        if (parser.match(TokenType.LEFT_PAREN)){
            thrower.SyntaxError("An interface may not have a constructor")
        }

        val block = if (parser.match(TokenType.LEFT_BRACE)){
            BlockStatement().visit(parser)
        }else null
        val end = parser.getLocation()
        return State.InterfaceDeclaration(id,params,block, BigLocation(start, end))
    }
}