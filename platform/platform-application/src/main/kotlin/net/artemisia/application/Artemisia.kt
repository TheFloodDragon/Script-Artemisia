package net.artemisia.application

import net.artemisia.compiler.Compiler
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            //Console().init()
            //val byteArray = Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).printf()
            //val filePath = "main.apc"

            //File(filePath).writeBytes(byteArray)

            //println("字节已成功写入到文件: $filePath")

           Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).save()

        }
    }
}