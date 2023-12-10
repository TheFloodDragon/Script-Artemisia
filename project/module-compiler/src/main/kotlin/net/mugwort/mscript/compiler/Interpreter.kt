package net.mugwort.mscript.compiler

import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.CoreFunction
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.expection.thrower
import net.mugwort.mscript.runtime.function.Function
import net.mugwort.mscript.runtime.function.NativeFunction


class Interpreter(private val code : String) {
    private var globals: Environment = Environment()
    private val parser : Parser = Parser(Lexer(code).tokens)
    private val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        CoreFunction(globals)
    }
    fun execute(){
        for (body in program.body){
            Statements().statement(body)
        }
    }
    inner class Statements{
        fun statement(body : Statement,parent: Environment? = null){
            when(body){
                is Statement.BlockStatement -> blockStatement(body)
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
                is Statement.VariableStatement -> Statements().varStatement(body,parent)
                is Statement.VisitorStatement -> TODO()
                is Statement.WhileStatement -> TODO()
                is Statement.ExpressionStatement -> Expressions().expressionStatement(body)
                else ->{

                }
            }
        }
        private fun varStatement(statement: Statement.VariableStatement, environment: Environment? = null){
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
            if (environment == null){
                define(globals)
            }else{
                define(environment)
            }
        }
        private fun blockStatement(statement: Statement.BlockStatement){
            val env = Environment(globals)
            for (body in statement.body){
                if (body is Statement.ReturnStatement){
                    returnStatement(body)
                }
                statement(body,env)
            }
        }
        private fun functionStatement(statement: Statement.FunctionDeclaration){
            try {
                blockStatement(statement.body)
            }catch (e : ReturnException){
                createFunction(statement,Expressions().expressionStatement(Statement.ExpressionStatement(e.expression)))
            }finally {
                createFunction(statement)
            }
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
                is Expression.Identifier -> identifier(expr,env)
                is Expression.GroupExpression -> group(expr)
                is Expression.BinaryExpression -> binary(expr)
                is Expression.CallExpression -> callFunction(expr)
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

        private fun binary(expr: Expression.BinaryExpression): Number {
            var left = if (expr.left !is Expression.NumericLiteral){
                expressionStatement(Statement.ExpressionStatement(expr.left))
            }else{
                (expr.left as Expression.NumericLiteral).value!!
            }
            val operator = expr.operator
            var right = if (expr.right !is Expression.NumericLiteral){
                expressionStatement(Statement.ExpressionStatement(expr.right))
            }else{
                (expr.right as Expression.NumericLiteral).value!!
            }
            if (left is Expression.GroupExpression){
               left = group(left)
            }
            if (right is Expression.GroupExpression){
                right = group(right)
            }

            return math(left.toString().toDouble(),operator,right.toString().toDouble())
        }

        private fun group(expr : Expression.GroupExpression): Number? {
            val type = expr.expr
            if (type is Expression.BinaryExpression){
                return binary(type)
            }
            return null
        }

        private fun math(left: Double,op : Any?,right: Double) : Number{
            return when(op){
                "+" -> (left + right)
                "-" -> (left - right)
                "/" -> (left / right)
                "%" -> (left % right)
                "*" -> (left * right)
                else -> {
                    return 0
                }
            }
        }


        private fun callFunction(expr: Expression.CallExpression): Any? {
            val params = arrayListOf<Any?>()
            for (param in expr.arguments){
                params.add(expressionStatement(Statement.ExpressionStatement(param)))
            }
            return runFunction(expr.caller.name,params)
        }
    }

    private fun createFunction(expr : Statement.FunctionDeclaration,ret : Any? = null): Function {
        return Function(expr,globals,ret)
    }

    private fun runFunction(function: String,params: List<Any?>): Any? {
        val func = globals.get(function) ?: thrower.RuntimeException("Cannot Found $function")
        if (func is NativeFunction){
            return func.call(params)
        }else{
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