package net.mugwort.artemisia.api

import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole

object Console {

    fun err(str:String){
        print("[")
        print( " err ",Ansi.Color.RED)
        print("] $str")
        print("",Ansi.Color.DEFAULT)
        println()
    }
    fun info(str:String){
        print("[")
        print( " info ",Ansi.Color.BLUE)
        print("] $str")
        print("",Ansi.Color.DEFAULT)
        println()
    }
    fun input(): String? {
        print("[ ")
        print("input",Ansi.Color.GREEN)
        print(" ] ")
        return readlnOrNull()
    }

    fun format(content: String, colour : Ansi.Color) : Ansi {
        return Ansi.ansi().fg(colour).a(content)

    }

    fun print(content : String, colour : Ansi.Color ) {
        AnsiConsole.systemInstall()
        print(format(content, colour))
        AnsiConsole.systemUninstall()
    }


    enum class Color(val code : String){
        Clear("0"),
        Black("30"),
        RED("31"),
        GREEN("32"),
        YELLOW("33"),
        BLUE("34"),
        FUCHSIA("35"),
        THING("36"),
        WHITE("37");
    }
    fun setColor(str: String,color : Color): String {
        return "\u001B[${color.code}m${str}\u001B[0m"
    }
    fun setColor(str: String,code : Int): String {
        return "\u001B[${code}m${str}\u001B[0m"
    }
}