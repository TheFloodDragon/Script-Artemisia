package net.mugwort.mscript.runtime

import net.mugwort.mscript.runtime.expection.thrower

open class Environment {
    private var env: Environment
    private var values: MutableMap<String, Any?> = mutableMapOf()
    private var consts : MutableMap<String,Any?> = mutableMapOf()
    constructor() {
        this.env = this
    }

    constructor(parent: Environment) {
        this.env = parent
    }

    fun get(key: String): Any? {
        if (values.keys.contains(key)) {
            return values[key]
        }
        if (consts.keys.contains(key)){
            return key
        }
        if (env != this) {
            return env.get(key)
        }
        return null
    }

    fun set(key: String, value: Any?) {
        if (values.keys.contains(key)){
            values[key] = value
            return
        }
        if (consts.keys.contains(key))thrower.SyntaxError("Cannot set const variable '$key'")
        if (env != this) {
            env.set(key,value)
            return
        }
        thrower.SyntaxError("Undefined variable '$key' to set.")
        return
    }
    fun define(key: String, value: Any?, const : Boolean = false){
        if (values.keys.contains(key)) {
            thrower.SyntaxError("cannot define variable '$key', it was defined")
        }
        if (consts.keys.contains(key)){
            thrower.SyntaxError("cannot define variable '$key', it was defined")
        }
        if (const){
            consts[key] = value
        }else{
            values[key] = value
        }

    }

}