package net.mugwort.artemisia.runtime.compiler.objects

import net.mugwort.artemisia.runtime.compiler.Object

class FunctionObject(
    val id : IdentifierObject,
    val args : ArrayList<ArgObject>,
    val codes : ArrayList<CodeObject>,
    val returnType : TypeObject

): Object() {
    override fun toByte(): ByteArray {
        val array = createArray()
        array.add(args.size.toByte())
        for (i in args){
            array.addAll(i.toByte().toList())
        }
        array.add(id.len.toByte())
        array.addAll(id.toByte().toList())
        array.add(codes.size.toByte())
        for (i in codes){
            array.addAll(i.toByte().toList())
        }
        array.add(0,array.size.toByte())
        return array.toByteArray()
    }
}