package net.mugwort.dev

import net.mugwort.dev.core.frontend.Lexer
import net.mugwort.dev.core.frontend.Parser


/*
* let Main() {
*   println("awa")
* }
* */


open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            val i = "def b(var a : Number,var b : Number){  } "
            //Lexer(i).printf()
            println(Parser(Lexer(i).tokens).parserJson())
        }
    }
}