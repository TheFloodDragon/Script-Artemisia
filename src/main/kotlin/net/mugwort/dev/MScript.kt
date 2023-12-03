package net.mugwort.dev

import net.mugwort.dev.core.frontend.Lexer
import net.mugwort.dev.core.frontend.Parser
import java.io.File


/*
* let Main() {
*   println("awa")
* }
* */


open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            val i = File(System.getProperty("user.dir") + "/scripts/main.ms").readText()
            println()
            //Lexer(i).printf()
            println(Parser(Lexer("",File(System.getProperty("user.dir") + "/scripts/main.ms")).tokens).parserJson())
        }
    }
}