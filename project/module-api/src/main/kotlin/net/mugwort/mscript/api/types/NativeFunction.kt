package net.mugwort.mscript.api.types

import net.mugwort.mscript.runtime.expection.thrower




open class NativeFunction{
    open var params : Int = 0
    private var caller : ((List<Any?>) -> Any?)? = null
    constructor()
    constructor(size : Int,call : (List<Any?>) -> Any?){
        this.params = size
        this.caller = call
    }
    open fun onCall(arguments: List<Any?>) : Any?{
        if (arguments.size > params){
            thrower.RuntimeException("Insufficient parameters!")
        }
        return if (caller != null){
            caller?.let { it(arguments) }
        }else{
            0
        }

    }

}