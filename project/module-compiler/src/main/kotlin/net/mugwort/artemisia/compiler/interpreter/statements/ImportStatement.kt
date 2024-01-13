package net.mugwort.artemisia.compiler.interpreter.statements

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.api.expection.thrower
import net.mugwort.artemisia.compiler.Parser
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement
import java.io.File

class ImportStatement(private val interpreter: Interpreter) : StatementExecutor() {
    override val self: StatementExecutor
        get() = this

    override fun execute(body: Statement, env: Environment?) {
        val import = body as Statement.ImportStatement
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
            val file = File(path)
            val envs = Environment()
            if (interpreter.bus.getRegister(file.name.substringBeforeLast(".")) != null){
                val module = interpreter.bus.getRegister(file.name.substringBeforeLast("."))
                interpreter.globals.define(file.name.substringBeforeLast("."),module)
            }else{
                val parser = Parser(file.readText(),file).parser()
                for (i in parser.body) {
                    executor(i,envs,interpreter)
                }
                interpreter.globals.define(file.name.substringBeforeLast("."),envs)
            }

        }
        getImport(import.file)
        val path = interpreter.file.parentFile?.path + "/" + id.joinToString(separator = "/") + ".ari"
        if (File(path).exists() || interpreter.bus.getRegister(File(path).name.substringBeforeLast(".")) != null) {
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
}
