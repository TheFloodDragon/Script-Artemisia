package net.mugwort.mscript.api.types

import net.mugwort.mscript.api.Environment

abstract class NativeEvent {
    private val env = Environment()
    abstract fun onInit()
    fun onEvent(environment: Environment? = null): Environment {
        if (environment != null) {
            env.defineAll(environment.getValue())
            return env
        }
        return env
    }
    fun <E> addParams(string: String,value : E){
        env.define(string,value)
    }
}