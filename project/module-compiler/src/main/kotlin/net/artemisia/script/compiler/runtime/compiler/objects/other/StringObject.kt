package net.artemisia.script.compiler.runtime.compiler.objects.other

import net.artemisia.script.compiler.runtime.compiler.Object

class StringObject(val string: String) : Object() {

    override fun toByte(): ByteArray {
        val array = createArray()
        array.addAll(string.toByteArray().toList())
        return array.toByteArray()
    }
}