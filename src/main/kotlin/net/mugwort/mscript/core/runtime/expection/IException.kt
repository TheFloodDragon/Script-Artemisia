package net.mugwort.mscript.core.runtime.expection

import net.mugwort.mscript.core.runtime.Translation
import net.mugwort.mscript.utils.Logger
import kotlin.system.exitProcess


open class IException(val id : String) {
    companion object{
        fun send(id : String,message : String){
            Logger.fatal("${Translation.ExceptionOf.get()} $id : $message")
            throw RuntimeException()
            exitProcess(0)
        }
    }


    fun send(message: String) {
        send(id, message)
    }
}
