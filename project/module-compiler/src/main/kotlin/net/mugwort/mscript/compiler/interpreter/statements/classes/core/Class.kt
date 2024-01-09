package net.mugwort.mscript.compiler.interpreter.statements.classes.core

import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower

class Class(private val declaration : Statement.ClassDeclaration, private val parent: Environment,) : ICallable{

    val env : Environment = Environment(parent)
    override val paramCount: Int
        get() = declaration.params?.size ?: 0

    override fun call(arguments: List<Any?>): Any {
        if (arguments.size > paramCount){
            thrower.RuntimeException("Insufficient parameters!")
        }

        if (declaration.params != null){
            for (i in 0 until paramCount) {
                if (arguments.size < paramCount){
                    thrower.RuntimeException("Error! lost some params")
                }
                if (arguments[i] !is Statement){

                    if (getArgumentType(arguments[i]) != declaration.params!![i].declarations.init) thrower.RuntimeException("Type not equals")
                    env.define(declaration.params!![i].declarations.id.name, arguments[i])
                }

            }
        }
        return env
    }
    private fun getArgumentType(argument: Any?) : Expression {
        return when(argument){
            is Number -> Expression.NumericLiteral(null)
            is String -> Expression.StringLiteral(null)
            is Boolean -> Expression.BooleanLiteral(null)
            is Expression.Identifier -> Expression.Identifier(argument.name)
            else -> Expression.ObjectLiteral(null)
        }
    }
    override fun toString(): String {
        return "Class"
    }
}