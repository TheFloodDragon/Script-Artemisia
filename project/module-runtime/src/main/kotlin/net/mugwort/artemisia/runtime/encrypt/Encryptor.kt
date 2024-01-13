package net.mugwort.artemisia.runtime.encrypt

import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement

class Encryptor(private val program : Statement.Program) {
    fun encode(): ArrayList<String> {
        val codes = ArrayList<String>()
        for (state in program.body){
            when(state){
                is Statement.BlockStatement -> {
                    for (i in state.body){

                    }
                    codes.add("${state.location.start.line}-${state.location.end.line}:block()")

                }
                is Statement.CaseDeclaration -> TODO()
                is Statement.ClassDeclaration -> TODO()
                is Statement.DoWhileStatement -> TODO()
                is Statement.EmptyStatement -> TODO()
                is Statement.EnumStatement -> TODO()
                is Statement.EventStatement -> TODO()
                is Statement.ExpressionStatement -> codes.add("${state.location.start.line}: ${expr(state.expression)}")
                is Statement.ForStatement -> TODO()
                is Statement.FunctionDeclaration -> TODO()
                is Statement.IfStatement -> TODO()
                is Statement.ImportStatement -> TODO()
                is Statement.Program -> TODO()
                is Statement.ReturnStatement -> TODO()
                is Statement.SwitchStatement -> TODO()
                is Statement.TryStatement -> TODO()
                is Statement.VariableDeclaration -> TODO()
                is Statement.VariableStatement -> TODO()
                is Statement.VisitorStatement -> TODO()
                is Statement.WhileStatement -> TODO()
            }
        }
        return codes
    }

    fun expr(expr : Expression) : String{
        when(expr){
            is Expression.AssignmentExpression -> return "A(${expr(expr.left)},${expr.operator},${expr(expr.right)})"
            is Expression.BinaryExpression -> return "B(${expr(expr.left)},${expr.operator},${expr(expr.right)})"
            is Expression.BooleanLiteral -> return Literal.Boolean.id + "(${expr.value})"
            is Expression.CallExpression -> {
                val args = ArrayList<String>()
                for (arg in expr.arguments){
                    args.add(expr(arg))
                }
                return "C(${expr(expr.caller)}:$args)"
            }
            is Expression.GroupExpression -> return "G(${expr(expr.expr)})"
            is Expression.Identifier -> return Literal.Identifier.id + "(${expr.name})"
            is Expression.LogicalExpression -> return "L(${expr(expr.left)},${expr.operator},${expr(expr.right)})"
            is Expression.MemberExpression -> return "M(${expr(expr.objectExpression)},${expr(expr.objectExpression)},${expr.computed}"
            Expression.NullLiteral -> return Literal.Null.id
            is Expression.NumericLiteral -> return Literal.Number.id + "(${expr.value})"
            is Expression.ObjectLiteral -> return Literal.Object.id + "(${expr.value})"
            is Expression.StringLiteral -> return Literal.String.id + "(${expr.value})"
            is Expression.UnaryExpression -> return "U(${expr(expr.argument)},${expr.operator},${expr.head})"
            Expression.VoidLiteral -> return Literal.Void.id
        }
    }

    enum class Literal(val id : kotlin.String){
        Number("num"),
        Boolean("bool"),
        Identifier("id"),
        Object("obj"),
        Null("null"),
        String("str"),
        Void("void")
    }


}