package net.mugwort.artemisia.compiler.interpreter

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.api.Artemisia
import net.mugwort.artemisia.compiler.Parser
import net.mugwort.artemisia.compiler.interpreter.expressions.Call
import net.mugwort.artemisia.compiler.interpreter.statements.StatementExecutor
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.api.expection.thrower
import java.io.File


class Interpreter(val file: File) {
    val bus = Artemisia.getBus()
    var globals : Environment
    private val parser: Parser = Parser(file.readText(),file)
    val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        globals = bus.getEnv()
    }

    fun execute() {
        for (body in program.body) {
            if (thrower.endProcess){
                return
            }
            StatementExecutor.executor(body,globals,this)
        }
        if (globals.get("main") != null) {
            for (body in program.body){
                if (body is Statement.FunctionDeclaration){
                    if (body.identifier.name == "main"){
                        val id = body.identifier
                        val calls = Expression.CallExpression(id, listOf(),body.location)
                        Call(this).caller(calls, listOf(), globals)
                    }
                }
            }

        }
    }
    fun printProgram(): Interpreter {
        println(programJson)
        return this
    }
}