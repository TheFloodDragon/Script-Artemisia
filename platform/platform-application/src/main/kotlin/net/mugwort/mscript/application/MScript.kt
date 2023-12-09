package net.mugwort.mscript.application

import net.mugwort.mscript.compiler.Lexer
import net.mugwort.mscript.compiler.Parser
import java.io.File

open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){

            //val i = File(System.getProperty("user.dir") + "/scripts/main.ms").readText()
            println(System.getProperty("user.dir"))
            //Lexer(i).printf()
           println(Parser(Lexer("", File(System.getProperty("user.dir") + "/scripts/main.ms")).tokens).parserJson())
        }
    }
}