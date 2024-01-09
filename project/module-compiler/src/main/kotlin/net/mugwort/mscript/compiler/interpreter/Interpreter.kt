package net.mugwort.mscript.compiler.interpreter

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.api.MScript
import net.mugwort.mscript.api.registries.FunctionRegistry
import net.mugwort.mscript.compiler.Parser
import net.mugwort.mscript.compiler.interpreter.expressions.Call
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import java.io.File


class Interpreter(val file: File) {
    val bus = MScript.getBus()
    var globals : Environment
    private val parser: Parser = Parser(file.readText(),file)
    val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        FunctionRegistry().register(bus)
        globals = bus.getEnv()
    }

    fun execute() {
        for (body in program.body) {
            StatementExecutor.executor(body,globals,this)
        }
        if (globals.get("main") != null) {
            Call(this).caller("main", listOf(), globals)
        }
    }
    fun printProgram(): Interpreter {
        println(programJson)
        return this
    }
}