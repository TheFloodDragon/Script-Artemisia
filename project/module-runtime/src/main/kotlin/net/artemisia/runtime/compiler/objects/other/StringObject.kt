package net.artemisia.runtime.compiler.objects.other

import net.artemisia.runtime.compiler.Object

class StringObject(val string: String) : Object() {

    override fun toByte(): ByteArray {
        val array = createArray()
        array.addAll(string.toByteArray().toList())
        return array.toByteArray()
    }
}