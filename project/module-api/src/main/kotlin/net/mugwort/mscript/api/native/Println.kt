package net.mugwort.mscript.api.native

import net.mugwort.mscript.api.types.NativeFunction

class Println : NativeFunction() {
    override var params: Int = 0
    override fun onCall(arguments: List<Any?>){
        println(arguments[0])
    }

}