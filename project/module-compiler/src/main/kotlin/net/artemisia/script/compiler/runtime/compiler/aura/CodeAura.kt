package net.artemisia.script.compiler.runtime.compiler.aura

import net.artemisia.script.compiler.runtime.compiler.builder.ASMCodeBuilder
import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import net.artemisia.script.compiler.runtime.compiler.types.ASMCode
import java.lang.StringBuilder

class CodeAura {
    private val datas : ArrayList<ASMCodeBuilder> = arrayListOf()
    fun add(type: ASMCode, value : ArrayList<ASMCodeHelper>, comment : String = "") {
        datas.add(ASMCodeBuilder(type,value,comment))
    }
    fun getData(): ArrayList<ASMCodeBuilder> {
        return datas
    }
    fun show(){
        for (i in datas){
            println("   $i")
        }
    }

    override fun toString(): String {
        val builder : StringBuilder = StringBuilder()
        for (i in datas){
            builder.appendLine("   $i")
        }
        return builder.toString()
    }
}