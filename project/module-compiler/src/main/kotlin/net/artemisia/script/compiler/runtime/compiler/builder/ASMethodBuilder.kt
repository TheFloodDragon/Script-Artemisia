package net.artemisia.script.compiler.runtime.compiler.builder

import net.artemisia.script.compiler.runtime.compiler.aura.CodeAura
import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import java.lang.StringBuilder

class ASMethodBuilder(val id : ASMCodeHelper,val type : ASMCodeHelper,val params : ArrayList<ASMCodeHelper>,val codes : CodeAura?) {
    fun show(){
        println(".method $id(${params.joinToString(",")}) -> $type  #定一个名为${id} 的方法")
        codes?.show()
    }

    override fun toString(): String {
        val b = StringBuilder()
        b.appendLine(".method $id (${params.joinToString(",")}) -> $type  #定一个名为${id} 的方法")
        b.appendLine(codes?.toString())
        return b.toString()
    }
}