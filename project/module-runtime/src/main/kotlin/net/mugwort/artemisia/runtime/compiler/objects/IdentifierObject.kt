package net.mugwort.artemisia.runtime.compiler.objects

import net.mugwort.artemisia.runtime.compiler.Object

/*
*  <len> <id>
*/


class IdentifierObject(val len : Int,val id: String) : Object(){
    override fun toByte(): ByteArray {
        val array = createArray()
        array.add(len.toByte())
        array.addAll(id.toByteArray().toList())
        return id.toByteArray()
    }

}