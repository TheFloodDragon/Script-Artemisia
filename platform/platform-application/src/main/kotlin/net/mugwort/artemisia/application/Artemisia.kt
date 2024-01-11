package net.mugwort.artemisia.application

import net.mugwort.artemisia.compiler.interpreter.Interpreter
import net.mugwort.artemisia.runtime.Console
import org.fusesource.jansi.Ansi
import java.io.File


open class Artemisia {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            //console()

            Interpreter(File(System.getProperty("user.dir") + "/scripts/main.mg")).printProgram()


        }
        fun console(){
            print("[ ")
            Console.print("title",Ansi.Color.BLUE)
            print(" ] ")
            Console.print("Mugwort ", Ansi.Color.GREEN)
            Console.print("Script",Ansi.Color.CYAN)
            Console.print(" - ", Ansi.Color.WHITE)
            Console.print("Version ",Ansi.Color.RED)
            Console.print("0.3.1\n",Ansi.Color.YELLOW)
            Console.info("Welcome to Use Artemisia of Application Mode")
            println("")

            val builder = ArrayList<String>()
            var code = builder.joinToString("\n")
            while (true){
                val input = Console.input()
                if (input?.lowercase() == "#run"){
                    val file = File("./cli-code.mg")
                    file.writeText(code)
                    file.createNewFile()
                    Interpreter(file).execute()
                    file.delete()

                }else if (input?.lowercase() == "#break"){
                    break
                }else if (input?.lowercase() == "#reset"){
                    builder.clear()
                } else{
                    input?.let { builder.add(it) }
                    code = builder.joinToString("\n")
                }
            }
        }
    }
}