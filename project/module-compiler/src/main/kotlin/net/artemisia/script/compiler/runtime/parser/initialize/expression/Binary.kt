package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import compiler.Parser
import compiler.runtime.parser.Expression

class Binary(val left : Expr) : Expression {
    override fun visit(parser: Parser): Expr {
        val op = parser.consume(parser.look().type)!!.value
        val right = parser.getExpr()
        return Expr.BinaryExpr(op,left,right)
    }
}