package net.mugwort.artemisia.api.registries

import net.mugwort.artemisia.api.IScriptBus
import net.mugwort.artemisia.api.MScript
import net.mugwort.artemisia.api.registry.DeferredRegister
import net.mugwort.artemisia.api.types.NativeFunction

class FunctionRegistry {
    private val Function : DeferredRegister<NativeFunction> = DeferredRegister<NativeFunction>().create(MScript.getBus().getEnv())

    val PRINTLN = Function.registry("println", NativeFunction(1) {arg ->
        println(arg[0])
    })
    fun register(bus: IScriptBus){
        Function.registry(bus)
    }
}