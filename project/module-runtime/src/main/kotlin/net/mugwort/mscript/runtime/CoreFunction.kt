package net.mugwort.mscript.runtime

import net.mugwort.mscript.runtime.function.NativeFunction

class CoreFunction(env: Environment) {
    private val map: MutableMap<String, NativeFunction> = mutableMapOf()
    init {
        enumValues<Core>().forEach { enumValue ->
            registry(enumValue.id, enumValue.func)
        }
        for (i in map.keys){
            env.define(i,map[i])
        }
    }
    private fun registry(id: String, func: NativeFunction) {
        map[id] = func
    }
    private enum class Core(val id: String, val func: NativeFunction){
        PRINTLN(
            "println",
            NativeFunction(1){_,args ->
                println(args[0])
            }
        ),
        PRINT(
        "print",
            NativeFunction(1){_,args ->
                print(args[0])
            }
        )
    }

}