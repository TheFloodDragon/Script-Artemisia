package net.mugwort.artemisia.api


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

        this.env.defineAll(env.getValue())
        this.env.defineAll(env.getConst())
        this.env.defineAll(env.getFunction())

        return this
    }
    fun getBus(): IScriptBus {
        return this
    }
}