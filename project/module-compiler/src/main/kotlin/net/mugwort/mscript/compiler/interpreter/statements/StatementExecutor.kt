package net.mugwort.mscript.compiler.interpreter.statements

import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.compiler.interpreter.expressions.ExpressionExecutor
import net.mugwort.mscript.compiler.interpreter.statements.block.BlockStatement
import net.mugwort.mscript.compiler.interpreter.statements.classes.ClassStatement
import net.mugwort.mscript.compiler.interpreter.statements.function.FunctionStatement
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment

abstract class StatementExecutor {

    abstract val self: StatementExecutor
    companion object{
        fun executor(body: Statement,env: Environment?,interpreter: Interpreter?) : Any?{
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
                is Statement.IfStatement -> TODO()

                is Statement.Program -> TODO()
                is Statement.ReturnStatement -> TODO()
                is Statement.SwitchStatement -> TODO()
                is Statement.TryStatement -> TODO()
                is Statement.VariableDeclaration -> TODO()

                is Statement.VisitorStatement -> TODO()
                is Statement.WhileStatement -> TODO()
                is Statement.EventStatement -> TODO()
            }
        }
    }

    abstract fun execute(body: Statement, env: Environment?) : Any?
}