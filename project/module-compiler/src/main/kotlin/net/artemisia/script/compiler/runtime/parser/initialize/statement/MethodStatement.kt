package net.artemisia.script.compiler.runtime.parser.initialize.statement

import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Statement
import net.artemisia.script.compiler.runtime.parser.initialize.expression.Identifier

class MethodStatement : Statement{
    override fun visit(parser: Parser): State.MethodDeclaration {
        val start = parser.getLocation()
        parser.consume(TokenType.METHOD)
        val id = Identifier().visit(parser)
        parser.consume(TokenType.LEFT_PAREN)
        val params : ArrayList<State.VariableDeclaration> = arrayListOf()
        while (parser.look().type != TokenType.RIGHT_PAREN){
            if (parser.match(TokenType.COMMA)) parser.consume(TokenType.COMMA)
            when (parser.look().type){
                TokenType.FINAL -> params.add(VariableStatement(true).visit(parser))
                else -> params.add(VariableStatement().visit(parser))
            }
        }
        parser.consume(TokenType.RIGHT_PAREN)

        val block = if (parser.match(TokenType.LEFT_BRACE)) BlockStatement().visit(parser) else null
        if (parser.match(TokenType.COLON)){
            parser.consume(TokenType.COLON)
            val type = parser.getExpr()
            val end = parser.getLocation()
            return State.MethodDeclaration(id,params,block,type, BigLocation(start, end))
        }
        val end = parser.getLocation()
        return State.MethodDeclaration(id,params,block,null, BigLocation(start, end))
    }
}