package net.mugwort.mscript.application


import net.mugwort.mscript.compiler.Interpreter
import java.io.File

open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            val code = File(System.getProperty("user.dir") + "/scripts/main.ms").readText()
            Interpreter(code).printProgram().execute()
        }
    }
}