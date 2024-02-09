package net.artemisia.runtime.compiler.objects.module

import net.artemisia.runtime.compiler.Object
import net.artemisia.runtime.compiler.objects.other.IdentifierObject

class FunctionObject(
    val id : IdentifierObject,
    val args : ArrayList<ArgObject>,
    val codes : ArrayList<CodeObject>,
    val returnType : TypeObject

): Object() {
    override fun toByte(): ByteArray {
        val array = createArray()
        array.addAll(id.toByte().toList())
        array.add(args.size.toByte())
        for (i in args){
            array.addAll(i.toByte().toList())
        }
        array.add((codes.size * 2).toByte())
        for (i in codes){
            array.addAll(i.toByte().toList())
        }
        array.add(returnType.toByte().size.toByte())
        array.addAll(returnType.toByte().toList())
        return array.toByteArray()
    }
}