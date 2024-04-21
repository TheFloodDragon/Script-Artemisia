package net.artemisia.script.compiler.runtime.compiler.core.module

import net.artemisia.script.compiler.runtime.compiler.core.code.CodePool

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
class ModuleCode(val index : Int,val id : String,val size : Int, val code : CodePool) {
    fun toBytes() : ArrayList<Byte>{
        val byte = arrayListOf<Byte>()
        byte.add(index.toByte())
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