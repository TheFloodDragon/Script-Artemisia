package vm.runtime.interpreter.method

import vm.runtime.interpreter.ICallable

typealias Caller = (List<Any?>) -> Any

class NativeMethod(val caller : Caller) : ICallable {
    override val paramsize: Int
        get() = 0

    override fun call(params: List<Any?>): Any {
        return caller(params)
    }
}