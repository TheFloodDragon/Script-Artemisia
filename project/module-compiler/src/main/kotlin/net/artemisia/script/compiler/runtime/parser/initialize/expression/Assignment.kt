package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import common.ast.State
import common.token.TokenType
import compiler.Parser
import compiler.runtime.parser.Expression

class Assignment(private val left : State) : Expression {
    override fun visit(parser: Parser): Expr {
        parser.consume(TokenType.EQUAL)
        val right = parser.getState()
        return Expr.AssignmentExpr(left,right)
    }
}