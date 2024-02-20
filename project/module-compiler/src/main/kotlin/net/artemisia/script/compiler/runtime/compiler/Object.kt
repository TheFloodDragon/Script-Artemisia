package net.artemisia.script.compiler.runtime.compiler

abstract class Object {
    fun createArray(): ArrayList<Byte> {
        return ArrayList()
    }

    abstract fun toByte(): ByteArray
}