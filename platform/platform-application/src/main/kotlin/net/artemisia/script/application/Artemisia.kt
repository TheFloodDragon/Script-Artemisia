package net.artemisia.script.application


import net.artemisia.script.compiler.runtime.compiler.aura.DataAura
import net.artemisia.script.compiler.runtime.compiler.types.DataType


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val data : DataAura = DataAura()
            data.add(DataType.DD,0.0)
            data.add(DataType.DS,"hello!")
            data.show()

//            println(
//                ModuleJson().parser(
//                    Parser(
//                    File(System.getProperty("user.dir") + "/scripts/Main.ap")
//                ).parser())
//            )
//            Compiler(File(System.getProperty("user.dir") + "/scripts/Main.ap")).compile()
        }
    }
}