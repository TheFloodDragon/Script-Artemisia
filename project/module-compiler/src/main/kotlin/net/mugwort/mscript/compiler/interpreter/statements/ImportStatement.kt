package net.mugwort.mscript.compiler.interpreter.statements

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.compiler.Parser
import net.mugwort.mscript.compiler.interpreter.Interpreter
import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.expection.thrower
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
            val parser = Parser(File(path).readText(),File(path)).parser()
            for (i in parser.body) {
                executor(i, interpreter?.globals,interpreter)
            }
        }
        getImport(import.file)
        val path = interpreter?.file?.parentFile?.path + "/" + id.joinToString(separator = "/") + ".mg"
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
}
