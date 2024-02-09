package net.artemisia.runtime.compiler.objects.module

import net.artemisia.runtime.compiler.Object
import net.artemisia.runtime.compiler.objects.other.IdentifierObject

class EventObject(val id : IdentifierObject,
                  private val listener : ArrayList<ArgObject>,
                  private val codes : ArrayList<CodeObject>) : Object() {
    override fun toByte(): ByteArray {
        val array = createArray()
        array.addAll(id.toByte().toList())
        array.add(listener.size.toByte())
        for (i in listener){
            array.addAll(i.toByte().toList())
        }
        array.add((codes.size * 2).toByte())
        for (i in codes){
            array.addAll(i.toByte().toList())
        }
        return array.toByteArray()
    }

}