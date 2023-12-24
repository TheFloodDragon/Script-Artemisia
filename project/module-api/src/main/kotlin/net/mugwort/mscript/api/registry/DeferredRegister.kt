package net.mugwort.mscript.api.registry

import net.mugwort.mscript.api.IScriptBus
import net.mugwort.mscript.runtime.Environment

class DeferredRegister<E : Any> {
    private var env :  Environment = Environment()
    private var register : RegistryObject = RegistryObject(env)


    fun create(env : Environment): DeferredRegister<E> {
        register = RegistryObject(env)
        this.env = env
        return DeferredRegister()
    }
    fun registry(name : String,caller: E): RegistryObject {
        return register.registry(name,caller)
    }
    fun registry(bus : IScriptBus): IScriptBus {
        return bus.mergeEnv(env)
    }


}

