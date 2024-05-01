package compiler.runtime.compiler.core.code

class CodeItem(val code : CodeType,val index : Int? = null) {
    fun getByteArray() : ArrayList<Byte>{
        val array = arrayListOf(code.byte)
        index?.toByte()?.let { array.add(it) }
        return array
    }

    override fun toString(): String {
        return "$code : $index"
    }
}