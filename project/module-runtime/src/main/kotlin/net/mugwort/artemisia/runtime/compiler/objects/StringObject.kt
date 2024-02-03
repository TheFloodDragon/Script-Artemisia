package net.mugwort.artemisia.runtime.compiler.objects

import net.mugwort.artemisia.runtime.compiler.Object

class StringObject(val string: String) :  Object() {
    override fun toByte(): ByteArray {
        val array  = createArray()
        array.add(string.length.toByte())
        array.addAll(string.toByteArray().toList())
        return array.toByteArray()
    }
}