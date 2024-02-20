package net.artemisia.script.compiler.runtime.compiler.objects.other

import net.artemisia.script.compiler.runtime.compiler.Object

/*
*  <len> <id>
*/


class IdentifierObject(val len: Int, val id: String) : Object() {
    override fun toByte(): ByteArray {
        val array = createArray()
        array.add(len.toByte())
        array.addAll(id.toByteArray().toList())
        return array.toByteArray()
    }

}