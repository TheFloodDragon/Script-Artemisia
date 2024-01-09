package net.mugwort.mscript.compiler.interpreter.expressions.objects

import net.mugwort.mscript.api.types.NativeFunction
import kotlin.String

class String(val str: String) : Object(str) {
    init{
        env.define("len",str.length,true)
        env.define("plus",NativeFunction(1){anies ->
            str.plus(anies[0].toString())
        })
        env.define("substring",NativeFunction(2){anies ->
            str.substring(anies[0].toString().toInt(),anies[1].toString().toInt())
        })
        env.define("lowercase",NativeFunction(0){anies ->
            str.lowercase()
        })
    }

}