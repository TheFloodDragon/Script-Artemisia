package vm.runtime.interpreter.core

import vm.runtime.interpreter.method.Caller
import vm.runtime.interpreter.method.NativeMethod

class NativeMethods(private val env: Environment) {
    val PRINTLN = register("println"){ p -> println(if (p.isEmpty()) "" else p[0]) }



    private fun register(id : String, value : Caller): String {
        val method = NativeMethod(value)
        env.define(id,method)
        return id
    }
}