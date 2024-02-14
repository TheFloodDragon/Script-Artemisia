package net.artemisia.api

import net.artemisia.api.expection.thrower
import net.artemisia.api.types.NativeFunction

class Environment {
    private var env: Environment

    private var values: MutableMap<String, Any?> = mutableMapOf()
    private var consts: MutableMap<String, Any?> = mutableMapOf()
    private var functions: MutableMap<String, Any?> = mutableMapOf()

    constructor() {
        this.env = this
    }

    constructor(parent: Environment) {
        this.env = parent
    }

    fun getValue(): MutableMap<String, Any?> {
        return values
    }

    fun getConst(): MutableMap<String, Any?> {
        return consts
    }

    fun getFunction(): MutableMap<String, Any?> {
        return functions
    }

    fun addFunction(key: String, value: Any?) {
        functions[key] = value
    }

    fun get(key: String): Any? {
        if (functions.keys.contains(key)) {
            return functions[key]
        }
        if (values.keys.contains(key)) {
            return values[key]
        }
        if (consts.keys.contains(key)) {
            return consts[key]
        }
        if (env != this) {
            return env.get(key)
        }
        return null
    }

    fun defineAll(value: MutableMap<String, Any?>) {
        for (i in value.keys) {
            define(i, value[i])
        }
    }

    fun set(key: String, value: Any?, isFunction: Boolean = false) {
        if (isFunction || value is NativeFunction) {
            functions[key] = value
            return
        }
        if (values.keys.contains(key)) {
            values[key] = value
            return
        }
        if (consts.keys.contains(key)) thrower.SyntaxError("Cannot set const variable '$key'")
        if (env != this) {
            env.set(key, value)
            return
        }
        thrower.SyntaxError("Undefined variable '$key' to set.")
        return
    }

    fun define(key: String, value: Any?, const: Boolean = false, isFunction: Boolean = false) {
        if (isFunction || value is NativeFunction) {
            functions[key] = value
        } else if (const) {
            consts[key] = value
        } else {
            values[key] = value
        }
    }

    override fun toString(): String {
        return mapOf(
            "values" to mutableMapOf(
                "keys" to values.keys,
                "values" to values.values.toString()
            ), "const" to mutableMapOf(
                "keys" to consts.keys,
                "values" to consts.values
            ), "function" to mutableMapOf(
                "keys" to functions.keys,
                "values" to functions.values
            )
        ).toString()
    }
}