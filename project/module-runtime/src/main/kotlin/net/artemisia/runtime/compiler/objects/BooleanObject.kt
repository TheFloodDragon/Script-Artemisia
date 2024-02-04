package net.artemisia.runtime.compiler.objects

import net.artemisia.runtime.compiler.Object

class BooleanObject(val boolean: Boolean) : Object() {
    override fun toByte(): ByteArray {
        val array = createArray()
        if (boolean) array.add(0x01) else array.add(0x00)
        return array.toByteArray()
    }
}