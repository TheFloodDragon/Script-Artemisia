package net.artemisia.script.compiler.runtime.compiler.builder

import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import net.artemisia.script.compiler.runtime.compiler.types.ASMCode

class ASMCodeBuilder(val code: ASMCode,val helpers : ArrayList<ASMCodeHelper>,private val comment : String = "") {
    override fun toString(): String {
        return "$code ${helpers.joinToString(",")}      ${if (comment.isNotEmpty()) "#${comment}" else "" }"
    }

}