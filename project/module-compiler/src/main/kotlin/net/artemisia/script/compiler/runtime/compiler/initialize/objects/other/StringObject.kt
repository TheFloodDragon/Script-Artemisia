package compiler.runtime.compiler.initialize.objects.other

class StringObject {
    fun gen(value : String): List<Byte> {
        return value.toByteArray().toList()
    }
    fun decode(value : ByteArray): String {
        return String(value, charset("UTF-8"))
    }

}
