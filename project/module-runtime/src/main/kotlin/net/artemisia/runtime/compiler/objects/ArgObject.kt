package net.artemisia.runtime.compiler.objects

import net.artemisia.runtime.compiler.Object

class ArgObject(
    val id : IdentifierObject,
    val type : TypeObject,
    val value : Object
) : Object() {
    override fun toByte(): ByteArray {
        val arg = createArray()
        arg.addAll(type.toByte().toList())
        arg.addAll(id.toByte().toList())
        arg.addAll(value.toByte().toList())
        return arg.toByteArray()
    }

}