package net.artemisia.script.compiler.compiler

import java.nio.ByteBuffer

class DoubleObject(val d : Double) {
    fun parser(): ByteArray {
        return ByteBuffer.allocate(8).putDouble(d).array()
    }


}