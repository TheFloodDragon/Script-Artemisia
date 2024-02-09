package net.artemisia.runtime.compiler.objects.other

import net.artemisia.runtime.compiler.Object
import java.nio.ByteBuffer

class NumberObject(private val double: Double) : Object(){
    fun getLen() : Byte{
        if (isInt(double)) return byteArrayOf(double.toInt().toByte()).size.toByte()
        return doubleToBytes(double).size.toByte()
    }

    override fun toByte(): ByteArray {
        val array = createArray()
        if (isInt(double)){
            array.addAll(byteArrayOf(double.toInt().toByte()).toList())
            return array.toByteArray()
        }
        array.addAll(doubleToBytes(double).toList())

        return array.toByteArray()
    }

    fun doubleToBytes(value: Double): ByteArray {
        val buffer = ByteBuffer.allocate(java.lang.Double.BYTES)
        buffer.putDouble(value)
        return buffer.array()
    }
    fun isInt(value: Double): Boolean {
        return value == value.toInt().toDouble()
    }
}