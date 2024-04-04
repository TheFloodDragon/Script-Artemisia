package net.artemisia.script.compiler.compiler

import java.nio.ByteBuffer

class IntObject(val int : Int) {
    fun parser(): ByteArray {
        return ByteBuffer.allocate(4).putInt(int).array()
    }


}