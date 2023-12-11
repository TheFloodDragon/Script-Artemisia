package net.mugwort.mscript.compiler.runtime

import net.mugwort.mscript.compiler.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower


class Function(private val declaration: Statement.FunctionDeclaration,private val parent: Environment,private val interpreter : Interpreter) : ICallable {
    private var site: Any? = null
    override val paramCount: Int = declaration.params.count()
    override fun call(arguments: List<Any?>): Any? {
        val env = Environment(parent)
        val size = if (paramCount >= 0) paramCount else arguments.size

        for (i in 0 until size) {
            if (arguments.size < size){
                thrower.RuntimeException("Error! lost some params")
            }
            if (getArgumentType(arguments[i]) != declaration.params[i].declarations.init) thrower.RuntimeException("Type not equals")
            env.define(declaration.params[i].declarations.id.name, arguments[i])
            //println("${declaration.params[i].declarations.id.name} : ${arguments[i]}")
        }
        try {
            interpreter.Statements().blockStatement(declaration.body,env)
        }catch (e : Interpreter.ReturnException){
            return interpreter.Expressions().expressionStatement(Statement.ExpressionStatement(e.expression),env)
        }
        return null
    }
    private fun getArgumentType(argument: Any?) : Expression{
        return when(argument){
            is Number -> Expression.NumericLiteral(null)
            is String -> Expression.StringLiteral(null)
            is Boolean -> Expression.BooleanLiteral(null)
            else -> Expression.ObjectLiteral(null)
        }
    }
    override fun bind(site: Any?) {
        this.site = site
    }

    private fun unBind() {
        this.site = null
    }
}