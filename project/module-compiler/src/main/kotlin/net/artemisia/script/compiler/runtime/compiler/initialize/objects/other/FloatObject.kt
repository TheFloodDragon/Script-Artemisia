package net.artemisia.script.compiler.runtime.compiler.initialize.objects.other

import java.nio.ByteBuffer

class FloatObject {
    fun gen(value : Float): List<Byte> {
        val array = arrayListOf<Byte>()
        val byte = ByteBuffer.allocate(8)
        byte.putFloat(value)
        return byte.array().toList()
    }
}