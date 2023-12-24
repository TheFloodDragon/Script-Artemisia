package net.mugwort.mscript.application

import net.mugwort.mscript.api.MScript
import net.mugwort.mscript.compiler.interpreter.Interpreter
import java.io.File


open class MScript {
    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            testRegister().register(MScript.getBus())
            Interpreter(File(System.getProperty("user.dir") + "/scripts/main.mg")).execute()

        }
    }
}