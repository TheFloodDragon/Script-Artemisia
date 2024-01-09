package net.mugwort.mscript.application

import net.mugwort.mscript.api.IScriptBus
import net.mugwort.mscript.api.MScript
import net.mugwort.mscript.api.types.NativeFunction
import net.mugwort.mscript.api.registry.DeferredRegister

class testRegister {
    private val Function : DeferredRegister<NativeFunction> = DeferredRegister<NativeFunction>().create(MScript.getBus().getEnv())

    val LINE = Function.registry("test", NativeFunction(0) {arg ->
        println("test")
    })
    fun register(bus: IScriptBus){
        Function.registry(bus)
    }
}