package net.artemisia.script.compiler.runtime.compiler.initialize.objects.other

class StringObject {
    fun gen(value : String): List<Byte> {
        return value.toByteArray().toList()
    }


}
