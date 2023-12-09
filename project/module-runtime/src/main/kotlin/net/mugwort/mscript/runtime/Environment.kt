package net.mugwort.mscript.runtime

import net.mugwort.mscript.runtime.expection.thrower

class Environment {
    private var env: Environment
    private var values: MutableMap<String, Any?> = mutableMapOf()

    constructor() {
        this.env = this
    }

    constructor(env: Environment) {
        this.env = env
    }

    fun get(key: String): Any? {
        if (values.keys.contains(key)) {
            return values[key]
        }
        if (env != this) {
            return env.get(key)
        }
        thrower.SyntaxError("Undefined variable '$key'.")
        return null
    }

    fun set(key: String, value: Any?) {
        if (values.keys.contains(key)){
            values[key] = value
            return
        }
        if (env != this) {
            env.set(key,value)
            return
        }
        thrower.SyntaxError("Undefined variable '$key' to set.")
        return
    }
    fun defind(key: String,value: Any?){
        values[key] = value
    }

}