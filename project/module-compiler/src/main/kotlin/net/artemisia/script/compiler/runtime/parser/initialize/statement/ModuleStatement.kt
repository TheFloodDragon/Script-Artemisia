package compiler.runtime.parser.initialize.statement

import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Statement
import compiler.runtime.parser.initialize.expression.Identifier

class ModuleStatement(val identifier: Expr.Identifier? = null) : Statement{
    override fun visit(parser: Parser): State.Module {
        val start = parser.getLocation()

        if (parser.match(TokenType.MODULE)) parser.consume(TokenType.MODULE)
        val id = identifier ?: Identifier().visit(parser)
        if (parser.match(TokenType.LEFT_BRACE)){

            val body = BlockStatement().visit(parser).body as ArrayList
            val end = parser.getLocation()
            return State.Module(id, body, BigLocation(start, end))
        }
        val list : ArrayList<State> = arrayListOf()
        while (!parser.isEnd){
            if (parser.look().type == TokenType.EOF) break
            val item = parser.getState()
            list.add(item)
        }
        val end = parser.getLocation()
        return State.Module(id, list, BigLocation(start, end))
    }

}