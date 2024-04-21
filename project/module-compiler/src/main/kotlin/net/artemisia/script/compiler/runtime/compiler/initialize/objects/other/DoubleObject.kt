package net.artemisia.script.compiler.runtime.compiler.initialize.objects.other

import java.nio.ByteBuffer

class DoubleObject{
    fun gen(value : Double): List<Byte> {
        val array = arrayListOf<Byte>()
        val byte = ByteBuffer.allocate(8)
        byte.putDouble(value)
        return byte.array().toList()
    }
}