package net.mugwort.mscript.compiler.interpreter

import net.mugwort.mscript.compiler.Parser
import net.mugwort.mscript.compiler.interpreter.statements.classes.Class
import net.mugwort.mscript.compiler.interpreter.statements.classes.NativeClass
import net.mugwort.mscript.compiler.interpreter.statements.function.runtime.CoreFunction
import net.mugwort.mscript.compiler.interpreter.statements.function.runtime.Function
import net.mugwort.mscript.compiler.interpreter.statements.function.runtime.NativeFunction
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.expection.thrower
import java.io.File


class Interpreter(private val file: File) {
    private var globals: Environment = Environment()
    private val parser: Parser = Parser(file.readText())
    private val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        CoreFunction(globals)
        globals.define("Console", NativeClass(mutableMapOf("println" to CoreFunction.Core.PRINTLN.func)))
    }

    fun execute() {
        for (body in program.body) {
            Statements().statement(body, globals)
        }
        if (globals.get("main") != null) {
            runner("main", listOf(), globals)
        }
    }

    inner class Statements {
        fun statement(body: Statement, env: Environment) {
            when (body) {
                is Statement.BlockStatement -> blockStatement(body, env)
                is Statement.CaseDeclaration -> TODO()
                is Statement.ClassDeclaration -> classStatement(body,env)
                is Statement.DoWhileStatement -> TODO()
                is Statement.EmptyStatement -> TODO()
                is Statement.ForStatement -> TODO()
                is Statement.FunctionDeclaration -> functionStatement(body, env)
                is Statement.IfStatement -> TODO()
                is Statement.ImportStatement -> import(body, env)
                is Statement.ReturnStatement -> TODO()
                is Statement.SwitchStatement -> TODO()
                is Statement.TryStatement -> TODO()
                is Statement.VariableStatement -> Statements().varStatement(body, env)
                is Statement.VisitorStatement -> TODO()
                is Statement.WhileStatement -> TODO()
                is Statement.ExpressionStatement -> Expressions().expressionStatement(body, env)
                else -> {

                }
            }
        }

        private fun classStatement(classState : Statement.ClassDeclaration,env: Environment){
            val classEnv = createClass(classState,env).call(classState.params ?: listOf()) as Environment
            for (i in classState.body.body){
                statement(i,classEnv)
            }
        }

        private fun import(import: Statement.ImportStatement, env: Environment?) {
            val id: ArrayList<String> = arrayListOf()
            fun getImport(expr: Expression) {
                when (expr) {
                    is Expression.MemberExpression -> {
                        if (expr.objectExpression is Expression.Identifier) {
                            id.add((expr.objectExpression as Expression.Identifier).name)
                            getImport(expr.property)
                        } else {
                            thrower.RuntimeException("UnknownExpression")
                        }
                    }

                    is Expression.Identifier -> {
                        id.add(expr.name)
                    }

                    else -> {
                        thrower.RuntimeException("Cannot get Import")
                    }
                }
            }

            fun toImport(path: String) {
                val parser = Parser(File(path).readText()).parser()
                for (i in parser.body) {
                    Statements().statement(i, globals)
                }
            }
            getImport(import.file)
            val path = file.parentFile.path + "/" + id.joinToString(separator = "/") + ".mg"
            if (File(path).exists()) {
                toImport(path)
            } else {
                if (File(path).isDirectory) thrower.RuntimeException("The File is Directory")
                if (File(System.getProperty("user.dir") + id.joinToString(separator = "/")).exists()) {
                    toImport(System.getProperty("user.dir") + id.joinToString(separator = "/"))
                } else {
                    thrower.RuntimeException("Cannot import file!")
                }
            }
        }

        private fun varStatement(statement: Statement.VariableStatement, environment: Environment) {
            fun define(environment: Environment) {
                val id = statement.declarations.id.name
                val const = statement.const
                val init = statement.declarations.init?.let {
                    Statement.ExpressionStatement(
                        it
                    )
                }?.let { Expressions().expressionStatement(it) }
                if (const) {
                    environment.define(id, init, const)
                } else {
                    environment.define(id, init, const)
                }
            }
            define(environment)

        }

        fun blockStatement(statement: Statement.BlockStatement, env: Environment) {
            for (body in statement.body) {
                if (body is Statement.ReturnStatement) {
                    returnStatement(body)
                }
                statement(body, env)
            }
        }

        private fun functionStatement(statement: Statement.FunctionDeclaration, env: Environment?): Unit? {
            return if (env?.get(statement.identifier.name) == null) {
                newFunction(statement, env)
            } else {
                null
            }
        }

        private fun returnStatement(result: Statement.ReturnStatement) {
            if (result.argument == null) {
                throw ReturnException(Expression.NullLiteral, "return")
            } else {
                throw ReturnException(result.argument!!, "return")
            }
        }
    }

    inner class Expressions {
        fun expressionStatement(body: Statement.ExpressionStatement, env: Environment? = null): Any? {
            return when (val expr = body.expression) {
                is Expression.Identifier -> {
                    return identifier(expr, env)
                }
                is Expression.MemberExpression -> member(expr, env)
                is Expression.GroupExpression -> group(expr, env)
                is Expression.BinaryExpression -> binary(expr, env)
                is Expression.CallExpression -> call(expr, env)
                is Expression.NullLiteral, is Expression.StringLiteral, is Expression.BooleanLiteral, is Expression.ObjectLiteral, is Expression.NumericLiteral -> literal(
                    expr
                )
                else -> {
                    null
                }
            }
        }

        private fun member(member: Expression.MemberExpression, env: Environment?) {
            if (!member.computed) {
                if(member.objectExpression is Expression.CallExpression){
                    val args : ArrayList<Any?> = arrayListOf()
                    for (lie in (member.objectExpression as Expression.CallExpression).arguments){
                        args.add(literal(lie))
                    }
                    val get = runner(
                        (member.objectExpression as Expression.CallExpression).caller.name,
                        args,
                        env
                    ) as Environment

                    expressionStatement(Statement.ExpressionStatement(member.property),get)
                }else{
                    val get = expressionStatement(Statement.ExpressionStatement(member.objectExpression), env)
                    if (get is NativeClass) {
                        val classEnv = get.call(listOf()) as? Environment
                        expressionStatement(Statement.ExpressionStatement(member.property), classEnv)
                    }else if (get is Class){
                        val classEnv = get.call(listOf()) as? Environment
                        expressionStatement(Statement.ExpressionStatement(member.property), classEnv)
                    }
                }
            }
        }

        private fun identifier(id: Expression.Identifier, env: Environment? = null): Any? {
            if (env == null) {
                return globals.get(id.name)
            } else {
                return env.get(id.name)
            }
        }

        private fun binary(expr: Expression.BinaryExpression, env: Environment?): Any {
            var left = if (expr.left is Expression.NumericLiteral) {
                (expr.left as Expression.NumericLiteral).value!!
            } else if (expr.left is Expression.StringLiteral) {
                (expr.left as Expression.StringLiteral).value!!
            } else {
                expressionStatement(Statement.ExpressionStatement(expr.left), env)

            }
            val operator = expr.operator
            var right = if (expr.right is Expression.NumericLiteral) {
                (expr.right as Expression.NumericLiteral).value!!
            } else if (expr.right is Expression.StringLiteral) {
                (expr.right as Expression.StringLiteral).value!!
            } else {
                expressionStatement(Statement.ExpressionStatement(expr.right), env)

            }
            if (left is Expression.GroupExpression) {
                left = group(left, env)
            }
            if (right is Expression.GroupExpression) {
                right = group(right, env)
            }


            return math(left, operator, right)
        }

        private fun group(expr: Expression.GroupExpression, env: Environment?): Any? {
            val type = expr.expr
            if (type is Expression.BinaryExpression) {
                return binary(type, env)
            }
            return null
        }

        private fun math(left: Any?, op: Any?, right: Any?): Any {
            if (left is String) {
                return left + right
            } else {
                return when (op) {
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

        private fun call(expr: Expression.CallExpression, env: Environment?): Any? {
            val params = arrayListOf<Any?>()
            for (param in expr.arguments) {
                if (env != null) {

                    params.add(expressionStatement(Statement.ExpressionStatement(param), env))
                } else {
                    params.add(expressionStatement(Statement.ExpressionStatement(param)))
                }
            }
            return runner(expr.caller.name, params, env)
        }

    }



    private fun createClass(statement: Statement.ClassDeclaration,env: Environment): Class {
        val clazz = Class(statement,env)
        env.define(statement.identifier.name, clazz)
        return clazz
    }
    
    private fun newFunction(state: Statement.FunctionDeclaration, env: Environment?): Unit? {
        return env?.define(state.identifier.name, Function(state, globals, this))
    }





    private fun runner(calls: String, params: List<Any?>, env: Environment?): Any? {
        for (statement in program.body) {
            if (statement is Statement.FunctionDeclaration) {
                if (statement.identifier.name == calls) {
                    if (env?.get(calls) == null) {
                        newFunction(statement, env)
                    }
                }
            }
        }
        val caller = env?.get(calls) ?: thrower.RuntimeException("Cannot Found $calls")

        when (caller) {
            is Class -> {
                return caller.call(params)
            }
            is NativeClass ->{
                return caller.call(params)
            }
            is NativeFunction -> {
                return caller.call(params)
            }

            is Function -> {
                return caller.call(params)
            }

            else -> {
                thrower.RuntimeException("The Variable $calls isn`t Function")
            }
        }
        return null
    }

    class ReturnException(val expression: Expression, message: String) : Exception(message)

    private fun literal(expr: Expression?): Any? {
        return when (expr) {
            is Expression.Identifier -> {
                expr.name
            }

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

    fun printProgram(): Interpreter {
        println(programJson)
        return this
    }
}