package net.artemisia.script.application


import compiler.Compiler
import compiler.Parser
import gson.ModuleJson
import vm.ByteParser
import vm.Interpreter
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

//            println( ModuleJson().parser(
//                Parser(File(System.getProperty("user.dir") + "/scripts/Main.ap")).parser()
//            ))


            val compiler = Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).save()
            println(ByteParser(compiler.byteCodeFile).module)
//
//
            val interpreter = Interpreter(compiler.byteCodeFile)
            interpreter.debug().run()
        }
    }
}