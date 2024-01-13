package net.mugwort.artemisia.compiler.interpreter.statements

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.artemisia.compiler.interpreter.statements.block.BlockStatement
import net.mugwort.artemisia.compiler.interpreter.statements.classes.ClassStatement
import net.mugwort.artemisia.compiler.interpreter.statements.function.FunctionStatement
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.api.Console

abstract class StatementExecutor {

    abstract val self: StatementExecutor
    companion object{
        fun executor(body: Statement, env: Environment?, interpreter: Interpreter) : Any?{
            return when(body){
                is Statement.ExpressionStatement -> ExpressionExecutor.executor(body.expression,env,interpreter)
                is Statement.BlockStatement -> BlockStatement(interpreter).execute(body,env)
                is Statement.FunctionDeclaration -> FunctionStatement(interpreter).execute(body, env)
                is Statement.ClassDeclaration -> ClassStatement().execute(body,env)
                is Statement.ImportStatement -> ImportStatement(interpreter).execute(body, env)
                is Statement.VariableStatement -> VariableStatement(interpreter).execute(body, env)

                is Statement.CaseDeclaration -> TODO()
                is Statement.DoWhileStatement -> TODO()
                is Statement.EmptyStatement -> TODO()
                is Statement.ForStatement -> TODO()
                is Statement.IfStatement -> IfStatement(interpreter).execute(body,env)

                is Statement.Program -> executor(body,env,interpreter)
                is Statement.ReturnStatement -> TODO()
                is Statement.SwitchStatement -> TODO()
                is Statement.TryStatement -> TODO()
                is Statement.VariableDeclaration -> TODO()

                is Statement.WhileStatement -> TODO()
                is Statement.EventStatement -> TODO()
                else ->{
                    Console.err("Cannot Found Statement $body")
                }
            }
        }
    }

    abstract fun execute(body: Statement, env: Environment?) : Any?
}