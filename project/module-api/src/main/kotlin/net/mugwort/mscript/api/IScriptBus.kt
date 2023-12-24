package net.mugwort.mscript.api

import net.mugwort.mscript.runtime.Environment


class IScriptBus {
    private var env = Environment()

    fun getEnv(): Environment {
        return env
    }
    fun setEnv(environment: Environment): IScriptBus {
        this.env = environment
        return this
    }
    fun mergeEnv(env: Environment): IScriptBus {
        this.env.defindAll(env.getValues())
        return this
    }
    fun getBus(): IScriptBus {
        return this
    }
}