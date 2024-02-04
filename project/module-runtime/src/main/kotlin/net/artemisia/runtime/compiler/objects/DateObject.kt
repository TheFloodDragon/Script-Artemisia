package net.artemisia.runtime.compiler.objects

import net.artemisia.runtime.compiler.Object

class DateObject(val y : Int,val m : Int,val d : Int,val h : Int, val mm : Int) : Object(){
    override fun toByte(): ByteArray {
        val array = array()
        return array.toByteArray()
    }

    fun array(): ArrayList<Byte> {
        val byteArray = ArrayList<Byte>()

        // 添加长度信息
        byteArray.add(5.toByte())

        byteArray.addAll(intToBytes(y))
        byteArray.addAll(intToBytes(m))
        byteArray.addAll(intToBytes(d))
        byteArray.addAll(intToBytes(h))
        byteArray.addAll(intToBytes(mm))

        return byteArray
    }

    private fun intToBytes(value: Int): List<Byte> {
        return listOf(
            ((value shr 24) and 0xFF).toByte(),
            ((value shr 16) and 0xFF).toByte(),
            ((value shr 8) and 0xFF).toByte(),
            (value and 0xFF).toByte()
        )
    }
}