package net.artemisia.application.command.stdlib

import net.artemisia.application.command.Command
import net.artemisia.application.command.CommandInfo

@CommandInfo(name = "echo", info = "打印消息", use = "echo [message]")
class Echo : Command() {
    override fun execute(args: List<String>): Int {
        println(args[0])
        return 0
    }
}