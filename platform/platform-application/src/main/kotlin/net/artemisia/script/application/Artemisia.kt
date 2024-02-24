package net.artemisia.script.application

import net.artemisia.script.compiler.Parser
import net.artemisia.script.gson.ModuleJson
import java.io.File


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            println(
                ModuleJson().parser(
                    Parser(
                    File(System.getProperty("user.dir") + "/scripts/Main.ap")
                ).parser())
            )
        }
    }
}