package net.artemisia.script.compiler.runtime.compiler.objects.module

import net.artemisia.script.compiler.runtime.compiler.Object

class TypeObject(val type: Type, val obj: Object? = null) : Object() {
    enum class Type(val id: Byte) {
        BOOLEAN(0x19),
        STRING(0x20),
        NUMBER(0x21),
        IDENTIFIER(0x22),
        VOID(0x04)
    }

    override fun toByte(): ByteArray {
        val list: ArrayList<Byte> = arrayListOf()
        list.add(type.id)
        if (obj != null) {
            list.addAll(obj.toByte().toList())
        } else list.add(0x00)

        return list.toByteArray()
    }
}