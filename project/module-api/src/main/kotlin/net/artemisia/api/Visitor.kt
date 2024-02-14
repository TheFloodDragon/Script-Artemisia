package net.artemisia.api

import net.artemisia.api.expection.thrower
import net.artemisia.api.types.NativeFunction

class Visitor {
    private var visitor: Visitor

    constructor() {
        this.visitor = this
    }

    constructor(parent: Visitor) {
        this.visitor = parent
    }


    private var values: MutableMap<String, Any?> = mutableMapOf()
    private var consts: MutableMap<String, Any?> = mutableMapOf()
    private var functions: MutableMap<String, Any?> = mutableMapOf()

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
        if (visitor != this) {
            return visitor.get(key)
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
        if (visitor != this) {
            visitor.set(key, value)
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


}