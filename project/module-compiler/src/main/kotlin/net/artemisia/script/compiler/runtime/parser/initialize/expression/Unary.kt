package net.artemisia.script.compiler.runtime.parser.initialize.expression

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.compiler.Parser
import net.artemisia.script.compiler.runtime.parser.Expression

class Unary(val head : Boolean) : Expression {
    override fun visit(parser: Parser): Expr {
        return if (head){
            val op = parser.consume(parser.look().type)!!.value
            val right = parser.getExpr()
            Expr.UnaryExpr(op,right,head)
        }else{
            val right = parser.getExpr()
            val op = parser.consume(parser.look().type)!!.value
            Expr.UnaryExpr(op,right,head)
        }

    }
}