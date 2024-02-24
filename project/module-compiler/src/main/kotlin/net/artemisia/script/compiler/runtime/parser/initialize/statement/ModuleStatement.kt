package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement
import net.artemisia.script.compiler.runtime.parser.initialize.expression.Identifier

class ModuleStatement : Statement{
    override fun visit(parser: Parser): State.Module {
        parser.consume(TokenType.MODULE)
        val start = parser.getLocation()
        val id = Identifier().visit(parser)
        if (parser.match(TokenType.LEFT_BRACE)){

            val body = BlockStatement().visit(parser).body as ArrayList
            val end = parser.getLocation()
            return State.Module(id, body, BigLocation(start, end))
        }
        val list : ArrayList<State> = arrayListOf()
        while (!parser.isEnd){
            if (parser.look().type == TokenType.EOF) break
            list.add(parser.getState())
        }
        val end = parser.getLocation()
        return State.Module(id, list, BigLocation(start, end))
    }

}