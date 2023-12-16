package net.mugwort.mscript.application

import net.mugwort.mscript.compiler.interpreter.Interpreter
import java.io.File


open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            Interpreter(File(System.getProperty("user.dir") + "/scripts/main.mg")).execute()
            //Lexer(code).printf()

        }
    }
}