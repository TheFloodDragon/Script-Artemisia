package net.artemisia.runtime.compiler.objects.module

import net.artemisia.runtime.compiler.Object

class VisitorObject(val type: VisitorType, val obj : VisitObject, val index : Int) : Object(){
    enum class VisitorType(val i: Byte) {
        PUBLIC(0x01),
        PRIVATE(0x02),
        PROTECTED(0x03),
        ALREADY(0x04)
    }
    enum class VisitObject(val i : Byte){
        VARIABLE(0x11),
        FUNCTION(0x02),
        CLASS(0x03)
    }

    override fun toByte(): ByteArray {
        val array = createArray()
        array.add(type.i)
        array.add(obj.i)
        array.add(index.toByte())
        return array.toByteArray()
    }


}