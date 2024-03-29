package net.artemisia.script.common.ast

import net.artemisia.script.common.location.BigLocation


sealed class Expr {

    data class Identifier(val name: String) : Expr()


    data object VoidLiteral : Expr()
    data class NumericLiteral(val value: Number) : Expr()
    data class StringLiteral(val value: String) : Expr()
    data class BooleanLiteral(val value: Boolean) : Expr()
    data object NullLiteral : Expr()
    data class BinaryExpr(val operator: Any?, val left: Expr, val right: Expr) : Expr()
    data class GroupExpr(val state: State) : Expr()
    data class UnaryExpr(val operator: String, val argument: Expr, val head: Boolean) : Expr()
    data class MemberExpr(val left: Expr, val right: Expr, val computed: Boolean) : Expr()
    data class CallExpr(val caller: Expr, val arguments: List<State>, val location: BigLocation) : Expr()
    data class Lambda(val params : List<State.VariableDeclaration>,val body : State.BlockState): Expr()
    data class AssignmentExpr(val left: Expr, val operator: String, val right: Expr) : Expr()
    data class LogicalExpr(val operator: String, val left: Expr, val right: Expr) : Expr()
    data class GenericExpr(val id : Identifier,val generic : List<Expr>) : Expr()

    data class ToExpr(val left: Expr, val right: Expr) : Expr()
}