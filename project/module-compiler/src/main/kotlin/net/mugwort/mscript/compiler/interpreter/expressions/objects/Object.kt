package net.mugwort.mscript.compiler.interpreter.expressions.objects

import net.mugwort.mscript.runtime.Environment


class Object(id : Any?) {
    private val env : Environment = Environment()
    init {
//        env.define("toString",
//            NativeFunction(0){
//            id.toString()
//        })
    }

}