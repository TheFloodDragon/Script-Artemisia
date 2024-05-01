package compiler.runtime.parser.initialize.expression

import common.ast.Expr
import compiler.Parser
import compiler.runtime.parser.Expression

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