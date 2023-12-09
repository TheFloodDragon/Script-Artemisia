package net.mugwort.mscript

import net.mugwort.mscript.core.compiler.Lexer
import net.mugwort.mscript.core.compiler.Parser
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
            // println(1 +1)
            //Lexer(i).printf()
            println(Parser(Lexer("",File(System.getProperty("user.dir") + "/scripts/main.ms")).tokens).parserJson())
        }
    }
}