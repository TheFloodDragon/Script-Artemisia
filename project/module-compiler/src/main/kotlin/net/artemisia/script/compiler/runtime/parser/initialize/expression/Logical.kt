package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Logical(private val left : Expr) : Expression{
    override fun visit(parser: Parser): Expr {
        val op = parser.consume(parser.look().type)!!.type.id
        val right = parser.getExpr()
        return Expr.LogicalExpr(op,left, right)
    }


}