package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Assignment(private val left : State) : Expression {
    override fun visit(parser: Parser): Expr {
        parser.consume(TokenType.EQUAL)
        val right = parser.getState()
        return Expr.AssignmentExpr(left,right)
    }
}