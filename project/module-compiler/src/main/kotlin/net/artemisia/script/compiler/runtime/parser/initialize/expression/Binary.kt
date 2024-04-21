package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Binary(val left : Expr) : Expression {
    override fun visit(parser: Parser): Expr {
        val op = parser.consume(parser.look().type)!!.value
        val right = parser.getExpr()
        return Expr.BinaryExpr(op,left,right)
    }
}