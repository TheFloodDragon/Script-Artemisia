package net.mugwort.mscript

import net.mugwort.mscript.core.frontend.Lexer
import net.mugwort.mscript.core.frontend.Parser
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