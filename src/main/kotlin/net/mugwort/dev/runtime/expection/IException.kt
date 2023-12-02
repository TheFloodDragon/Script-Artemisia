package net.mugwort.dev.runtime.expection

import net.mugwort.dev.runtime.Translation
import net.mugwort.dev.utils.Logger
import kotlin.system.exitProcess


open class IException(val id : String) {
    companion object{
        fun send(id : String,message : String){
            Logger.fatal("${Translation.ExceptionOf.get()} $id : $message")
            exitProcess(0)
        }
    }


    fun send(message: String) {
        send(id, message)
    }
}
