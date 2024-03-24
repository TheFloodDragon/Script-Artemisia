package net.artemisia.script.compiler.runtime.compiler.aura

import net.artemisia.script.compiler.runtime.compiler.builder.ASMDataBuilder
import net.artemisia.script.compiler.runtime.compiler.types.DataType

class DataAura {
    private var index = 0
    private val datas : ArrayList<ASMDataBuilder> = arrayListOf()
    fun add(dataType: DataType,value : Any): Int {
        datas.add(ASMDataBuilder(dataType,index.toString(),value))
        index += 1
        return index
    }
    fun getData(): ArrayList<ASMDataBuilder> {
        return datas
    }
    fun show(){
        println(".data")
        for (i in datas){
            println("   $i")
        }
    }


}