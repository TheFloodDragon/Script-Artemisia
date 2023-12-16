package net.mugwort.mscript.compiler.interpreter

import net.mugwort.mscript.compiler.Parser
import net.mugwort.mscript.compiler.interpreter.expressions.Call
import net.mugwort.mscript.compiler.interpreter.statements.StatementExecutor
import net.mugwort.mscript.compiler.interpreter.statements.classes.core.NativeClass
import net.mugwort.mscript.compiler.interpreter.statements.function.core.CoreFunction
import net.mugwort.mscript.compiler.interpreter.statements.function.core.NativeFunction
import net.mugwort.mscript.runtime.Environment
import java.io.File


class Interpreter(val file: File) {
    var globals: Environment = Environment()
    private val parser: Parser = Parser(file.readText())
    val program = parser.parser()
    private val programJson = parser.parserJson()

    init {
        CoreFunction(globals)
        globals.define("printProgram",NativeFunction(0){_ ->
            printProgram()
        })
        globals.define("Console", NativeClass(mutableMapOf("println" to CoreFunction.Core.PRINTLN.func)))
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