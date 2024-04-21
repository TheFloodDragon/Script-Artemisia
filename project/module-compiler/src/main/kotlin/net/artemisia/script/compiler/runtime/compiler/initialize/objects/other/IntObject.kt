package net.artemisia.script.compiler.runtime.compiler.initialize.objects.other

import java.nio.ByteBuffer

class IntObject {
    fun gen(value : Int): List<Byte> {
        val array = arrayListOf<Byte>()
        val byte = ByteBuffer.allocate(4)
        byte.putInt(value)
        return byte.array().toList()
    }
}