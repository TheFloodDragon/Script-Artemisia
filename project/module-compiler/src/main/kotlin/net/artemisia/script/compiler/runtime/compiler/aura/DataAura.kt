package net.artemisia.script.compiler.runtime.compiler.aura

import net.artemisia.script.compiler.runtime.compiler.builder.ASMDataBuilder
import net.artemisia.script.compiler.runtime.compiler.types.DataType

class DataAura {
    private var index = 0
    private val datas : ArrayList<ASMDataBuilder> = arrayListOf()
    fun add(dataType: DataType,value : Any): Int {
        val adder = ASMDataBuilder(dataType,index.toString(),value)
        if (search(dataType,value) != -1) return search(dataType,value)
        datas.add(adder)
        val i = index
        index += 1
        return i
    }
    fun getData(): ArrayList<ASMDataBuilder> {
        return datas
    }
    fun search(dataType: DataType,value : Any): Int {
        for (i in datas){
            if (i.value == value){
                return i.id.toInt()
            }
        }
        return -1
    }
    fun get(index : Int): ASMDataBuilder {
        return datas[index]
    }

    fun show(){
        println(".data")
        for (i in datas){
            println("   $i")
        }
    }


}