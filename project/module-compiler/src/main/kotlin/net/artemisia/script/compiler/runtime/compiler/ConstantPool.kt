package net.artemisia.script.compiler.runtime.compiler

class ConstantPool(private val pool: MutableMap<Int, ArrayList<Byte>> = mutableMapOf()) {
    private var index = 0


    //index | type | len | value
    fun push(type: Byte, value: ByteArray) {
        val list: ArrayList<Byte> = arrayListOf()

        list.add(type)
        list.add(value.size.toByte())
        list.addAll(value.toList())

        pool[index] = list
        index += 1
    }

    fun push(v: ArrayList<Byte>) {
        val list: ArrayList<Byte> = arrayListOf()

        list.addAll(v.toList())

        pool[index] = list
        index += 1
    }

    fun getPool(): MutableMap<Int, ArrayList<Byte>> {
        return pool
    }

    fun get(i: Int): ArrayList<Byte>? {
        return pool[i]
    }

    fun search(value: ArrayList<Byte>): Int? {
        val entry = pool.entries.find { it.value == value }
        return entry?.key
    }

    fun search(value: List<Byte>): Int? {
        val entry = pool.entries.find { it.value == value }
        return entry?.key
    }

}