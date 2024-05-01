package vm.runtime

import compiler.runtime.compiler.core.data.DataPool

class Module(val version: List<Byte>, val type: Byte, val data: DataPool, val codes: List<CodeBlock>) {
    override fun toString(): String {
        val t = if (type == 0x0A.toByte()){
            "module"
        }else "class"

        return "Artemisia:\n version:$version \n type:$t \n datas:  $data \n codes:\n ${codes.joinToString(separator = "\n")}"
    }

}