package net.artemisia.script.common.expection

import kotlin.system.exitProcess


open class IException(val id: String) {
    companion object {
        fun send(id: String, message: String) {
            println("Error $id : $message")
            exitProcess(0)
        }
    }


    fun send(message: String) {
        send(id, message)
    }
}
