package net.artemisia.api.types

import net.artemisia.api.expection.thrower


open class NativeFunction {
    open var params: Int = 0
    private var caller: ((List<Any?>) -> Any?)? = null

    constructor()
    constructor(params: Int, call: (List<Any?>) -> Any?) {
        this.params = params
        this.caller = call
    }

    constructor(params: Params, call: (List<Any?>) -> Any?) {
        this.params = params.params.size
        this.caller = call
    }

    open fun onCall(arguments: List<Any?>): Any? {
        if (arguments.size > params) {
            thrower.RuntimeException("Insufficient parameters!")
        }
        return if (caller != null) {
            caller?.let { it(arguments) }
        } else {
            0
        }

    }

}