package net.artemisia.runtime.compiler.objects.module

import net.artemisia.runtime.compiler.Object
import net.artemisia.runtime.compiler.objects.other.IdentifierObject

class ArgObject(
    val id : IdentifierObject,
    val type : TypeObject,
) : Object() {
    override fun toByte(): ByteArray {
        val arg = createArray()
        arg.add(id.toByte().size.toByte())
        arg.addAll(id.toByte().toList())
        arg.add(type.toByte().size.toByte())
        arg.addAll(type.toByte().toList())
        return arg.toByteArray()
    }

}