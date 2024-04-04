package net.artemisia.script.compiler.runtime.compiler.helper

class ASMCodeHelper(val type: ValueType,val value : Any) {
    enum class ValueType(val value : String,val byte: Byte){
        DATA("",0x0A),
        POINTER("%",0x0B),
        LOADCONSTANT("$",0x0C)
    }

    override fun toString(): String {


        return "${type.value}$value"
    }

}