package net.mugwort.artemisia.application.command.stdlib

import net.mugwort.artemisia.application.command.Command
import net.mugwort.artemisia.application.command.CommandInfo
import net.mugwort.artemisia.application.command.CommandProcessor

@CommandInfo(name = "help", info = "显示帮助信息", use = "help")
class Help(private val process: CommandProcessor) : Command() {
    override fun execute(args: List<String>): Int {
        val helpMessage = StringBuilder()
        helpMessage.append("UsePage -> \n")
        for (command in process.commands.values) {
            val info = command::class.java.getAnnotation(CommandInfo::class.java)
            if (info != null) {

                helpMessage.append("    ${info.use} - ${info.info}\n")
            }
        }
        println(helpMessage.toString())
        return 0
    }
}