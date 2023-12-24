package net.mugwort.mscript.api.registries

import net.mugwort.mscript.api.IScriptBus
import net.mugwort.mscript.api.MScript
import net.mugwort.mscript.api.function.NativeFunction
import net.mugwort.mscript.api.registry.DeferredRegister

class FunctionRegistry {
    private val Function : DeferredRegister<NativeFunction> = DeferredRegister<NativeFunction>().create(MScript.getBus().getEnv())

    val PRINTLN = Function.registry("println", NativeFunction(0) {arg ->
        println(arg[0])
    })
    fun register(bus: IScriptBus){
        Function.registry(bus)
    }
}