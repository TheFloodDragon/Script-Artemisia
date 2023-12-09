package net.mscript.windows

open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){


            //val i = File(System.getProperty("user.dir") + "/scripts/main.ms").readText()
            println(System.getProperty("user.dir"))
            //Lexer(i).printf()
           // println(Parser(Lexer("",File(System.getProperty("user.dir") + "/scripts/main.ms")).tokens).parserJson())
        }
    }
}