package net.artemisia.vm

class ConstantPool {
    val pool: MutableMap<Int, ArrayList<Any>> = mutableMapOf()
    fun add(index: Int, value: ArrayList<Any>) {
        pool[index] = value
    }
}