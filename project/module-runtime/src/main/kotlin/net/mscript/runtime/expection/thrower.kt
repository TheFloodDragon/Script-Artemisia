package net.mscript.runtime.expection

import net.mscript.runtime.Translation

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
}