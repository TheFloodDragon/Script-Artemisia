package net.artemisia.api.expection

import net.artemisia.api.Console
import net.artemisia.runtime.other.Translation
import kotlin.system.exitProcess


open class IException(val id: String) {
    companion object {
        fun send(id: String, message: String) {
            Console.err("${Translation.ExceptionOf.get()} $id : $message")
            exitProcess(0)
        }
    }


    fun send(message: String) {
        send(id, message)
    }
}
