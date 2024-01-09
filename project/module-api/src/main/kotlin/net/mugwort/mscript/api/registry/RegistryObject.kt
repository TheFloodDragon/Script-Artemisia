package net.mugwort.mscript.api.registry

import net.mugwort.mscript.api.Environment

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


    fun registry(key : String,value : Any,isFunction : Boolean) : RegistryObject{

        if (isFunction){
            env?.define(key,value,false,true)
        }else{
            env?.define(key,value)
        }
        //println(env)
        return RegistryObject(key,value,env)
    }
    fun getId(): String? {
        return key
    }
    fun getValue(): Any? {
        return value
    }
}