package net.mugwort.artemisia.compiler.interpreter.statements.classes

import net.mugwort.artemisia.compiler.interpreter.statements.StatementExecutor
import net.mugwort.artemisia.compiler.interpreter.statements.classes.core.Class
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.api.Environment

class ClassStatement : StatementExecutor() {
    override val self: StatementExecutor
        get() = this

    override fun execute(body: Statement, env: Environment?): Environment? {
        return env?.let { newClazz((body as Statement.ClassDeclaration), it) }?.env
    }

    private fun newClazz(statement: Statement.ClassDeclaration, env: Environment): Class {
        val clazz = Class(statement,env)
        env.define(statement.identifier.name, clazz)
        return clazz
    }
}