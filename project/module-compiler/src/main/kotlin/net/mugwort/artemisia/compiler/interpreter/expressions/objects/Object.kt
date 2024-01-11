package net.mugwort.artemisia.compiler.interpreter.expressions.objects

import net.mugwort.artemisia.api.types.NativeFunction
import net.mugwort.artemisia.api.Environment


open class Object(val id : Any?) {
    val env : Environment = Environment()
    init {
        env.define("this",id)
        env.define("toString",
            NativeFunction(0){
                id.toString()
        })
        env.define("equals", NativeFunction(1){ args ->
            id?.equals(args)
        })
    }
}