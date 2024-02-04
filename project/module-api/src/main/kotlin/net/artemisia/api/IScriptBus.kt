package net.artemisia.api

import net.artemisia.api.module.Module


class IScriptBus {
    private var map : MutableMap<String, Environment> = mutableMapOf()
    private var env = Environment()

    fun getEnv(): Environment {
        return env
    }
    fun setEnv(environment: Environment): IScriptBus {
        this.env = environment
        return this
    }
    fun registerModule(module: Module){
        val id = module.id
        val env = module.ModuleEnv()
        map[id] = env
    }
    fun getRegister(id : String): Environment? {
        return map[id]
    }

    fun mergeEnv(env: Environment): IScriptBus {

        this.env.defineAll(env.getValue())
        this.env.defineAll(env.getConst())
        this.env.defineAll(env.getFunction())

        return this
    }
    fun getBus(): IScriptBus {
        return this
    }
}