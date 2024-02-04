package net.artemisia.runtime.compiler.objects

import net.artemisia.core.asm.ByteCode
import net.artemisia.runtime.compiler.Object

class CodeObject(
    private val command : ByteCode,
) : Object() {

    override fun toByte(): ByteArray {
        val list : ArrayList<Byte> = arrayListOf()
        list.add(command.toArray().size.toByte())
        list.addAll(command.toArray().toList())
        return list.toByteArray()
    }
}