
import net.mugwort.artemisia.application.command.Command
import net.mugwort.artemisia.application.command.CommandInfo
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

@CommandInfo(name = "edit", info = "启动简单文本编辑器", use = "edit [filename] - 启动编辑指定文件")
class Edit : Command() {
    private lateinit var fileName: String
    private val content = StringBuilder()

    override fun execute(args: List<String>): Int {
        fileName = args[0]
        try {
            content.clear()
            val file = File(fileName)
            if (file.exists()) {
                content.append(file.readText())
            } else {
                file.mkdirs()
                file.createNewFile()
            }
            startEditorLoop()
            file.writeText(content.toString())

            println("文件已成功保存：$fileName")
        } catch (e: Exception) {
            println("编辑器发生错误：$e")
            return 1
        }
        return 0
    }

    private fun startEditorLoop() {
        val scanner = Scanner(System.`in`)
        var lineNumber = 1
        while (true) {
            println("${lineNumber}: ${content.toString().substring(0, Math.min(80, content.length))}")
            println(":q - 退出编辑器")
            val input = scanner.nextLine()
            if (input.isEmpty()) {
                continue
            }
            if (input == ":q") {
                break
            }
            if (input.startsWith(":w")) {
                try {
                    val fileWriter = FileWriter(fileName)
                    fileWriter.write(content.toString())
                    fileWriter.close()
                } catch (e: IOException) {
                    println("保存文件时发生错误：$e")
                }
                continue
            }
            if (input.startsWith(":wq")) {
                try {
                    val fileWriter = FileWriter(fileName)
                    fileWriter.write(content.toString())
                    fileWriter.close()
                } catch (e: IOException) {
                    println("保存文件时发生错误：$e")
                }
                break
            }
            if (input.startsWith(":")) {
                println("未知命令")
                continue
            }
            content.append(input)
            content.append("\n")
            lineNumber++
        }
        scanner.close()
    }
}
