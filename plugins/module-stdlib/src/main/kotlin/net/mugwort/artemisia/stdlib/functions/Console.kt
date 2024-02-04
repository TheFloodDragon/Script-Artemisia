package net.mugwort.artemisia.stdlib.functions

import net.artemisia.api.Environment
import net.artemisia.api.module.Module
import net.artemisia.api.types.NativeFunction
import net.artemisia.api.types.Object
import net.artemisia.api.types.Params


class Console : Module("console"){
    override fun ModuleEnv(): Environment {
        env.addFunction("println", NativeFunction(Params(listOf(Object.OBJ()))){ arg ->
            println(arg[0])
        })
        return env
    }
}