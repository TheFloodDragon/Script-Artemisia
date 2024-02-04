package net.artemisia.application.command

import net.artemisia.api.Console
import java.util.*

class CommandProcessor  {
    val commands = mutableMapOf<String, Command>()

    fun registerCommand(command: Command) {
        val commandInfoAnnotation = command::class.java.getAnnotation(CommandInfo::class.java)
        if (commandInfoAnnotation != null) {
            commands[commandInfoAnnotation.name.lowercase(Locale.getDefault())] = command
        }
    }

    fun process(input: String): Int {
        val parts = input.split(" ")
        val commandName = parts[0].lowercase(Locale.getDefault())

        val command = commands[commandName]
        return if (command != null) {
            val args = parts.subList(1, parts.size)
            command.execute(args)
        } else {
            Console.err("未知命令: $commandName")
            return 0
        }
    }
}