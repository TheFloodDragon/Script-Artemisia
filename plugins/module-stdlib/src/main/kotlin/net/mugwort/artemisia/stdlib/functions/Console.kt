package net.mugwort.artemisia.stdlib.functions

import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.api.module.Module
import net.mugwort.artemisia.api.types.NativeFunction
import net.mugwort.artemisia.api.types.Object
import net.mugwort.artemisia.api.types.Params


class Console : Module("console"){
    override fun ModuleEnv(): Environment {
        env.addFunction("println",NativeFunction(Params(listOf(Object.OBJ()))){ arg ->
            println(arg[0])
        })
        return env
    }
}