package net.mugwort.artemisia.application.command.stdlib

import net.mugwort.artemisia.application.command.Command
import net.mugwort.artemisia.application.command.CommandInfo

@CommandInfo(name = "echo", info = "打印消息", use = "echo [message]")
class Echo : Command() {
    override fun execute(args: List<String>): Int {
        println(args[0])
        return 0
    }
}