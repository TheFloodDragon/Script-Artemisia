package net.mugwort.artemisia.application

import net.mugwort.artemisia.api.Console
import net.mugwort.artemisia.application.command.CommandProcessor
import net.mugwort.artemisia.application.command.stdlib.Echo
import net.mugwort.artemisia.application.command.stdlib.Help
import net.mugwort.artemisia.application.plugin.Plugins
import org.fusesource.jansi.Ansi

class Console {
    val plugin = Plugins()
    fun init(){
        print("[ ")
        Console.print("title", Ansi.Color.BLUE)
        print(" ] ")
        Console.print("Artemisia", Ansi.Color.GREEN)
        Console.print(" - ", Ansi.Color.WHITE)
        Console.print("Version ", Ansi.Color.RED)
        Console.print("0.3.1\n", Ansi.Color.YELLOW)
        Console.info("Welcome to Use Artemisia of Application Mode")
        println("")

        plugin.manager.addPlugin("Artemisia",0.3)
        plugin.init()

        println(" ")

        val processor = CommandProcessor()

        processor.registerCommand(Echo())
        processor.registerCommand(Help(processor))

        while (true) {
            val input = Console.input()

            if (input != null) {
                if (input.isNotEmpty()) {
                    processor.process(input)
                }
            }
        }
    }
}