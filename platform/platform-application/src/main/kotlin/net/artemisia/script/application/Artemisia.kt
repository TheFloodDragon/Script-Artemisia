package net.artemisia.script.application


import net.artemisia.script.compiler.Compiler
import net.artemisia.script.compiler.runtime.compiler.aura.CodeAura
import net.artemisia.script.compiler.runtime.compiler.aura.DataAura
import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import net.artemisia.script.compiler.runtime.compiler.types.ASMCode
import net.artemisia.script.compiler.runtime.compiler.types.DataType
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val cm = Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap"))
            cm.compiler()
            cm.show()
        }
    }
}