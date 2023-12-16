package net.mugwort.mscript.compiler.interpreter.statements.function.runtime

import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower

typealias Caller = (List<Any?>) -> Any?
class NativeFunction(arity: Int, private val caller: Caller) : ICallable {
    private var site: Any? = null

    override val paramCount: Int = arity
    override fun call(arguments: List<Any?>): Any? {
        if (arguments.size > paramCount){
            thrower.RuntimeException("Insufficient parameters!")
        }
        return caller(arguments)
    }
    override fun toString(): String {
        return "NativeFunction"
    }
}