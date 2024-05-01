package compiler.runtime.compiler.core.module

import compiler.runtime.compiler.core.code.CodePool
import compiler.runtime.compiler.core.code.CodeType

/*
* <code>
*
*   <index>
*   <id>
*   <size>
*   [
*
*
*         *code*
*
*
*   ]
*
*
* */
class ModuleCode(val index : Int, val id : String, val type : BlockType, val size : Int, val code : CodePool) {
    enum class BlockType(val byte: Byte){
        METHOD(0x0A),
        NORMAL(0x0B),
        MODULE(0x0C);
        companion object{
     
            fun fromByte(id: Byte): BlockType? {
                return BlockType.entries.firstOrNull { it.byte == id }
            }
        }
    }
    enum class BlockVisitor(val byte: Byte){
        Public(0x00),
        Private(0x01),
        Override(0x02),
        Abstract(0x03);

        companion object{
     
            fun fromByte(id: Byte): BlockVisitor? {
                return BlockVisitor.entries.firstOrNull { it.byte == id }
            }
        }
    }


    fun toBytes() : ArrayList<Byte>{
        val byte = arrayListOf<Byte>()
        byte.add(index.toByte())
        byte.add(type.byte)
        byte.add(size.toByte())
        byte.add(id.length.toByte())
        byte.addAll(id.toByteArray().toList())
        byte.addAll("{".toByteArray().toList())
        var i = 0
        code.getDatas().forEach {
            byte.add(i.toByte())
            byte.addAll(it.getByteArray())
            i += 1
        }
        byte.addAll("}".toByteArray().toList())

        return byte
    }




}