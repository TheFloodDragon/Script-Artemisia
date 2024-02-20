package net.artemisia.script.common.expection


import net.artemisia.script.common.location.BigLocation
import java.io.File
import kotlin.system.exitProcess

object thrower {
    var endProcess = false

    private class RuntimeException : IException("RuntimeException")
    private class SyntaxError : IException("SyntaxError")

    @JvmStatic
    fun RuntimeException(message: String) {
        RuntimeException().send(message)

    }

    @JvmStatic
    fun SyntaxError(message: String) {
        SyntaxError().send(message)
    }

    @JvmStatic
    fun send(msg: String, err: String, file: File, location: BigLocation, end: Boolean = false) {
        println(
            "${file.name}:${location.start.line}:${location.end.column} : ${err} : ${msg}\n" + draw(
                file,
                location
            )
        )
        endProcess = true
        if (end) exitProcess(0)
    }

    private fun draw(file: File, location: BigLocation): String {
        val texts = file.readLines()
        val errorLine = texts[location.start.line - 1]
        val modified = StringBuilder()
        modified.append("${location.start.line} | " + errorLine)

        val lines = StringBuilder(" ".repeat(errorLine.length))

        lines.insert(modified.indexOf("|"), "| ")
        val insertIndex = (modified.indexOf("|") + location.start.column).coerceAtMost(lines.length)
        lines.insert(insertIndex, " ^")

        for (i in lines.indexOf("^") + 1 until modified.length) {
            lines.setCharAt(i, '~')
        }
        return "                    $modified\n" +
                "                    $lines"
    }

}