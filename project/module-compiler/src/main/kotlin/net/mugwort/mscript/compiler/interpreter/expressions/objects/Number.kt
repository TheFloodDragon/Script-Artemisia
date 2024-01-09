package net.mugwort.mscript.compiler.interpreter.expressions.objects

import net.mugwort.mscript.api.types.NativeFunction

class Number(val num: Double) : Object(num) {
    init{
        env.define("plus", NativeFunction(1){ anies ->
            num.plus(anies[0].toString().toDouble())
        })
        env.define("minus", NativeFunction(1){ anies ->
            num.minus(anies[0].toString().toDouble())
        })
        env.define("times", NativeFunction(1){anies ->
            num.times(anies[0].toString().toDouble())
        })
        env.define("div", NativeFunction(1){anies ->
            num.div(anies[0].toString().toDouble())
        })
        env.define("mod", NativeFunction(1){anies ->
            num.mod(anies[0].toString().toDouble())
        })
    }
}