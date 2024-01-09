package net.mugwort.mscript.compiler.interpreter.expressions.objects

import net.mugwort.mscript.api.types.NativeFunction
import net.mugwort.mscript.api.Environment


open class Object(val id : Any?) {
    val env : Environment = Environment()
    init {
        env.define("this",id)
        env.define("toString",
            NativeFunction(0){
                id.toString()
        })
        env.define("equals",NativeFunction(1){args ->
            id?.equals(args)
        })
    }
}