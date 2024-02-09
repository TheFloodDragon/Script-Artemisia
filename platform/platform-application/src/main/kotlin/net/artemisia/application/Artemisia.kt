package net.artemisia.application

import net.artemisia.compiler.Compiler
import net.artemisia.vm.Decompilation
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).save()
            val parser = Decompilation(File(System.getProperty("user.dir") + "/scripts/Main.apc")).output()

        }
    }
}