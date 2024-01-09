package net.mugwort.mscript.api.registry

import net.mugwort.mscript.api.Environment
import net.mugwort.mscript.api.IScriptBus
import net.mugwort.mscript.api.types.NativeFunction

class DeferredRegister<E : Any> {
    private var env : Environment = Environment()
    private var register : RegistryObject = RegistryObject(env)


    fun create(env : Environment): DeferredRegister<E> {
        register = RegistryObject(env)
        this.env = env
        return DeferredRegister()
    }
    fun registry(name : String,caller: E): RegistryObject {
        if (caller is NativeFunction){

            return register.registry(name,caller,true)
        }
        return register.registry(name,caller,false)
    }
    fun registry(bus : IScriptBus): IScriptBus {
        return bus.mergeEnv(env)
    }


}

