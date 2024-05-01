package compiler.runtime.compiler.core.data

import compiler.runtime.parser.initialize.statement.VariableStatement


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
        }else{
            return datas.values.indexOf(data)
        }
        return index - 1
    }

    fun search(index : Int): Any {
        return getDatas()[index]!!.values.first()
    }

    override fun toString(): String {
        val string = StringBuilder()
        datas.forEach{ (index, value) ->
            string.append("\n   $index -> {")
            val s = StringBuilder()
            value.forEach{ (id, value) ->
                s.appendLine("      id: $id")
                s.appendLine("      value: $value")
            }
            string.append("\n$s")
            string.append("     }")
        }
        return string.toString()
    }

}