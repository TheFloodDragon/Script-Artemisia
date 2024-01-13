package net.mugwort.artemisia.application

import net.mugwort.artemisia.api.Console
import net.mugwort.artemisia.api.plugins.PluginManager
import net.mugwort.artemisia.application.plugin.Plugins
import net.mugwort.artemisia.compiler.interpreter.Interpreter
import org.fusesource.jansi.Ansi
import java.io.File

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
        val builder = ArrayList<String>()
        var code = builder.joinToString("\n")
        while (true){
            val input = Console.input()
            when(input?.lowercase()){
                "#reload" -> {
                    Plugins().init()
                }
                "#plugins" -> {
                    PluginManager().getPlugins().forEach{id,version ->
                        Console.info("$id:$version")
                    }
                }
                "#run" -> {
                    val file = File("./cli-code.mg")
                    file.writeText(code)
                    file.createNewFile()
                    Interpreter(file).execute()
                    file.delete()
                }
                "#break" -> {
                    break
                }
                "#reset" -> {
                    builder.clear()
                }
                else -> {
                    input?.let { builder.add(it) }
                    code = builder.joinToString("\n")
                }
            }
        }
    }
}