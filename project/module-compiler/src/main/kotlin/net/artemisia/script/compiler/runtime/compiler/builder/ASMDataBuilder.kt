package net.artemisia.script.compiler.runtime.compiler.builder

import net.artemisia.script.compiler.runtime.compiler.types.DataType

class ASMDataBuilder(val type: DataType, val id : String, val value : Any) {
    override fun toString(): String {
        return "$type $id : $value"
    }
}