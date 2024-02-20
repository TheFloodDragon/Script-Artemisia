package net.artemisia.script.compiler.runtime.compiler.objects.module


import net.artemisia.script.common.vm.ByteCode
import net.artemisia.script.compiler.runtime.compiler.Object

class CodeObject(
    private val command: ByteCode,
) : Object() {

    override fun toByte(): ByteArray {
        val list: ArrayList<Byte> = arrayListOf()
        list.addAll(command.toArray().toList())
        return list.toByteArray()
    }
}