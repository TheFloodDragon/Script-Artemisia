package net.artemisia.script.compiler.runtime.compiler.core.data


/*
 *  <index> <type> <len> <value>
 *
 *
 *  data -> values
 *
 *
 *
 *
 *
 */
class DataPool  {
    private var index = 0
    private val datas = mutableMapOf<Int,MutableMap<DataType,Any>>()

    fun getDatas(): MutableMap<Int, MutableMap<DataType, Any>> = datas

    fun push(type: DataType,value : Any): Int {
        val data = mutableMapOf<DataType,Any>()
        data[type] = value
        if (!datas.values.contains(data)) {
            datas[index] = data
            this.index += 1
        }


        return index - 1
    }
}