package net.mugwort.mscript.api.registry

import net.mugwort.mscript.runtime.Environment

class RegistryObject {
    private var key : String? = null
    private var value : Any? = null
    private var env : Environment? = null

    constructor(env : Environment){
        this.env = env
    }
    constructor(key: String,value: Any,env: Environment? = null){
        this.key = key
        this.value = value
        this.env = env
    }


    fun registry(key : String,value : Any) : RegistryObject{
        env?.define(key,value)
        return RegistryObject(key,value,env)
    }
    fun getId(): String? {
        return key
    }
    fun getValue(): Any? {
        return value
    }
}