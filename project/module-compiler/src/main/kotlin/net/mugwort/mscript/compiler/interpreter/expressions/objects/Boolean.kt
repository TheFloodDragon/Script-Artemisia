package net.mugwort.mscript.compiler.interpreter.expressions.objects

import net.mugwort.mscript.api.types.NativeFunction
import kotlin.Boolean

class Boolean(val bool: Boolean) : Object(bool) {
    init{
        env.define("and",NativeFunction(1){anies ->
            bool.and(anies[0].toString().toBoolean())
        })
        env.define("or",NativeFunction(1){anies ->
            bool.or(anies[0].toString().toBoolean())
        })
        env.define("not",NativeFunction(0){
            bool.not()
        })
    }
}