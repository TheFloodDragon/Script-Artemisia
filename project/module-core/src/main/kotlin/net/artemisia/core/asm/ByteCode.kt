package net.artemisia.core.asm

abstract class ByteCode(val byte: Byte) {

    data class LoadModule(val moduleFile : ByteArray) : ByteCode(0x4A){
        override fun toArray(): ByteArray {
            val array : ArrayList<Byte> = arrayListOf()
            array.add(byte)
            array.addAll(moduleFile.toList())
            return array.toByteArray()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LoadModule

            return moduleFile.contentEquals(other.moduleFile)
        }

        override fun hashCode(): Int {
            return moduleFile.contentHashCode()
        }

    }

    data class Call(val callID : Byte) : ByteCode(0x2A){
        override fun toArray(): ByteArray {
            val array : ArrayList<Byte> = arrayListOf()
            array.add(byte)
            array.add(callID)
            return array.toByteArray()
        }
    }


    data class Push(val index : Byte) : ByteCode(0x22){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }

    data class EventListener(val index : Byte) : ByteCode(0x6A){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }


    data class CreateFunction(val index : Byte) : ByteCode(0x7A){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }


    data class SetVariable(val index : Byte) : ByteCode(0x11){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }


    data class SaveVariable(val index : Byte) : ByteCode(0x12){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }

    data class SaveConstant(val index : Byte) : ByteCode(0x13){
        override fun toArray(): ByteArray {
            return byteArrayOf(byte,index)
        }
    }

    data class InvokeType(val index : Byte) : ByteCode(0x14){
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