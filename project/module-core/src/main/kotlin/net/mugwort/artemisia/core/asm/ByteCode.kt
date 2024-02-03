package net.mugwort.artemisia.core.asm

abstract class ByteCode(val byte: Byte) {



    data class Push(val index : Byte) : ByteCode(0x22){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }
    data class CreateFunction(val index : Byte) : ByteCode(0x7A){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }
    data class SaveItem(val index : Byte) : ByteCode(0x12){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }

    object ADD : ByteCode(0x3A){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte)
        }
    }

    abstract fun toArray() : ByteArray

}