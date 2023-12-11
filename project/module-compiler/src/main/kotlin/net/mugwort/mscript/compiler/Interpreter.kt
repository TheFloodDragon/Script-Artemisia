package net.mugwort.mscript.compiler

import net.mugwort.mscript.compiler.runtime.CoreFunction
import net.mugwort.mscript.compiler.runtime.Function
import net.mugwort.mscript.compiler.runtime.NativeFunction
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.expection.thrower


class Interpreter(private val code : String) {
    private var globals: Environment = Environment()
    private val parser : Parser = Parser(code)
    private val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        CoreFunction(globals)
    }
    fun execute(){
        for (body in program.body){
            Statements().statement(body,globals)
        }
    }
    inner class Statements{
        fun statement(body : Statement,env: Environment){
            when(body){
                is Statement.BlockStatement -> blockStatement(body, env)
                is Statement.CaseDeclaration -> TODO()
                is Statement.ClassDeclaration -> TODO()
                is Statement.DoWhileStatement -> TODO()
                is Statement.EmptyStatement -> TODO()
                is Statement.ForStatement -> TODO()
                is Statement.FunctionDeclaration -> functionStatement(body)
                is Statement.IfStatement -> TODO()
                is Statement.ImportStatement -> TODO()
                is Statement.ReturnStatement -> TODO()
                is Statement.SwitchStatement -> TODO()
                is Statement.TryStatement -> TODO()
                is Statement.VariableStatement -> Statements().varStatement(body,env)
                is Statement.VisitorStatement -> TODO()
                is Statement.WhileStatement -> TODO()
                is Statement.ExpressionStatement -> Expressions().expressionStatement(body,env)
                else ->{

                }
            }
        }
        private fun varStatement(statement: Statement.VariableStatement, environment: Environment){
            fun define(environment: Environment){
                val id = statement.declarations.id.name
                val const = statement.const
                val init = statement.declarations.init?.let {
                    Statement.ExpressionStatement(
                        it
                    )
                }?.let { Expressions().expressionStatement(it) }
                if (const){
                    environment.define(id,init,const)
                }else{
                    environment.define(id,init,const)
                }
            }
            define(environment)

        }

        fun blockStatement(statement: Statement.BlockStatement,env: Environment){
            for (body in statement.body){
                if (body is Statement.ReturnStatement){
                    returnStatement(body)
                }
                statement(body,env)
            }
        }
        private fun functionStatement(statement: Statement.FunctionDeclaration){
            return createFunction(statement)
        }
        private fun returnStatement(result: Statement.ReturnStatement){
            if (result.argument == null){
                throw ReturnException(Expression.NullLiteral,"return")
            }else{
                throw ReturnException(result.argument!!,"return")
            }
        }

    }

    inner class Expressions{
        fun expressionStatement(body : Statement.ExpressionStatement,env: Environment? = null) : Any?{
            return when(val expr = body.expression){
                is Expression.Identifier -> {
                    return identifier(expr,env)
                }
                is Expression.GroupExpression -> group(expr,env)
                is Expression.BinaryExpression -> binary(expr,env)
                is Expression.CallExpression -> callFunction(expr,env)
                is Expression.NullLiteral,is Expression.StringLiteral,is Expression.BooleanLiteral,is Expression.ObjectLiteral,is Expression.NumericLiteral -> literal(expr)
                else -> {
                    null
                }
            }
        }

        private fun identifier(id:Expression.Identifier, env: Environment? = null): Any? {
            if (env == null){
                return globals.get(id.name)
            }else{
                return env.get(id.name)
            }
        }

        private fun binary(expr: Expression.BinaryExpression,env: Environment?): Any {
            var left = if (expr.left is Expression.NumericLiteral){
                (expr.left as Expression.NumericLiteral).value!!
            }else if (expr.left is Expression.StringLiteral){
                (expr.left as Expression.StringLiteral).value!!
            } else{
                expressionStatement(Statement.ExpressionStatement(expr.left),env)

            }
            val operator = expr.operator
            var right = if (expr.right is Expression.NumericLiteral){
                (expr.right as Expression.NumericLiteral).value!!
            }else if (expr.right is Expression.StringLiteral){
                (expr.right as Expression.StringLiteral).value!!
            } else{
                expressionStatement(Statement.ExpressionStatement(expr.right),env)

            }
            if (left is Expression.GroupExpression){
               left = group(left,env)
            }
            if (right is Expression.GroupExpression){
                right = group(right,env)
            }


            return math(left,operator,right)
        }

        private fun group(expr : Expression.GroupExpression,env: Environment?): Any? {
            val type = expr.expr
            if (type is Expression.BinaryExpression){
                return binary(type,env)
            }
            return null
        }

        private fun math(left: Any?,op : Any?,right: Any?) : Any{
            if (left is String){
                return left + right
            }else{
                return when(op){
                    "+" -> (left.toString().toDouble() + right.toString().toDouble())
                    "-" -> (left.toString().toDouble() - right.toString().toDouble())
                    "/" -> (left.toString().toDouble() / right.toString().toDouble())
                    "%" -> (left.toString().toDouble() % right.toString().toDouble())
                    "*" -> (left.toString().toDouble() * right.toString().toDouble())
                    else -> {
                        return 0
                    }
                }
            }
        }


        private fun callFunction(expr: Expression.CallExpression,env: Environment? = null): Any? {
            val params = arrayListOf<Any?>()
            for (param in expr.arguments) {
                if (env != null){
                    params.add(expressionStatement(Statement.ExpressionStatement(param),env))
                }else{
                    params.add(expressionStatement(Statement.ExpressionStatement(param)))
                }
            }
            return runFunction(expr.caller.name, params)
        }
    }

    private fun createFunction(expr : Statement.FunctionDeclaration) {
        return globals.define(expr.identifier.name, Function(expr,globals,this))
    }

    private fun runFunction(function: String,params: List<Any?>): Any? {
        val func = globals.get(function) ?: thrower.RuntimeException("Cannot Found $function")
        if (func is NativeFunction){
            return func.call(params)
        }else if (func is Function){
            return func.call(params)
        } else{
            thrower.RuntimeException("The Variable $function isn`t Function")
        }
        return null
    }

    class ReturnException(val expression: Expression, message: String) : Exception(message)

    private fun literal(expr: Expression?): Any? {
        return when (expr) {
            is Expression.ObjectLiteral -> {
                expr.value
            }
            is Expression.NullLiteral -> {
                null
            }

            is Expression.NumericLiteral -> {
                expr.value
            }

            is Expression.StringLiteral -> {
                expr.value
            }

            is Expression.BooleanLiteral -> {
                expr.value
            }
            else -> {
                null
            }
        }
    }
    fun printProgram() : Interpreter{
        println(programJson)
        return this
    }
}