package net.artemisia.vm
class Parser(val code : ByteArray) {
    val magic : ByteArray = code.sliceArray(0 until 4)
    val version = code.sliceArray(4 until 8)
    val index = 8

    fun getConstants(){

    }

}