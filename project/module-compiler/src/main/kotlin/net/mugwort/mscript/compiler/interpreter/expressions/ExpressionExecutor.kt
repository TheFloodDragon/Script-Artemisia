package net.mugwort.mscript.compiler.interpreter.expressions

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.runtime.Literal
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.runtime.Environment

abstract class ExpressionExecutor {
    abstract val self: ExpressionExecutor

    companion object{
        fun executor(body: Expression, env: Environment?,interpreter: Interpreter?) : Any?{
            return when(body){
                is Expression.CallExpression -> Call(interpreter).visit(body,env)
                is Expression.AssignmentExpression -> Assignment(interpreter).visit(body,env)
                is Expression.BinaryExpression -> Binary(interpreter).visit(body, env)
                is Expression.GroupExpression -> Group(interpreter).visit(body, env)
                is Expression.Identifier -> Identifier(interpreter).visit(body,env)
                is Expression.LogicalExpression -> Logical(interpreter).visit(body, env)
                is Expression.MemberExpression -> Member(interpreter).visit(body, env)
                is Expression.UnaryExpression -> Unary(interpreter).visit(body, env)
                is Expression.StringLiteral,is Expression.ObjectLiteral,is Expression.BooleanLiteral,is Expression.NumericLiteral,Expression.NullLiteral,Expression.VoidLiteral -> Literal(body).get()
            }
        }

    }

    abstract fun visit(body: Expression, env: Environment?) : Any?
}