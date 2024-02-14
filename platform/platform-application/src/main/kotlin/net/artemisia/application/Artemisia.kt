package net.artemisia.application

import net.artemisia.compiler.Parser
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).save()
            //val parser = Decompilation(File(System.getProperty("user.dir") + "/scripts/Main.apc")).output()
            println(Parser(File(System.getProperty("user.dir") + "/scripts/Main.ap").readText(), File(System.getProperty("user.dir") + "/scripts/Main.ap")).parserJson())
        }
    }
}