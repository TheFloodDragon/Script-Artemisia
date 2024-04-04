package net.artemisia.script.compiler.runtime.compiler.aura

import net.artemisia.script.compiler.runtime.compiler.builder.ASMethodBuilder
import java.io.File
import java.lang.StringBuilder

data class ProgramAura(
    val file: File,
    val id : String,
    val data : DataAura,
    val main : CodeAura,
    val methods : ArrayList<ASMethodBuilder>
){
    fun show(){
        println("[$id -> ${file.absolutePath}]")
        println()
        data.show()
        println()
        println(".main")
        main.show()
        println()
        for (i in methods){
            i.show()
            println()
        }
    }

    override fun toString(): String {
        val builder : StringBuilder = StringBuilder()
        builder.appendLine("[$id -> ${file.absolutePath}]")
        builder.appendLine(data.toString())
        builder.appendLine("")
        builder.appendLine(".main")
        builder.appendLine(main.toString())
        builder.appendLine("")
        for (i in methods){
            builder.appendLine(i.toString())
            builder.appendLine("")
        }
        return builder.toString()

    }
}