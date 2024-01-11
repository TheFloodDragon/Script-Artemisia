package net.mugwort.artemisia.runtime.expection

import net.mugwort.artemisia.core.ast.token.Token
import net.mugwort.artemisia.runtime.Console
import net.mugwort.artemisia.runtime.other.Translation
import java.io.File
import kotlin.system.exitProcess

object thrower {

    private class RuntimeException : IException(Translation.RuntimeException.get())
    private class SyntaxError : IException(Translation.SyntaxError.get())
    @JvmStatic
    fun RuntimeException(message:String){
        RuntimeException().send(message)

    }
    @JvmStatic
    fun SyntaxError(message:String){
        SyntaxError().send(message)
    }
    @JvmStatic
    fun send(msg : String,err:String,file : File,token : Token){
        Console.err("\n   $err: \n" +
                "       in File ${file.absoluteFile}\n" +
                "       line -> ${token.location.line}\n" +
                "       column -> ${token.location.column}\n" +
                "       $msg"
        )
        exitProcess(0)
    }

}