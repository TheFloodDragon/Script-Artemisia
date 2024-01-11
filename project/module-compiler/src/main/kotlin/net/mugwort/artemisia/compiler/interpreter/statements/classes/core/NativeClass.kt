package net.mugwort.artemisia.compiler.interpreter.statements.classes.core

import net.mugwort.artemisia.api.types.NativeFunction
import net.mugwort.artemisia.api.Environment
import net.mugwort.artemisia.runtime.ICallable
import net.mugwort.artemisia.runtime.expection.thrower

class NativeClass(private val functions:MutableMap<String, NativeFunction>) : ICallable {
    private val env : Environment = Environment()

    override val paramCount: Int
        get() = 0

    override fun call(arguments: List<Any?>): Any {
        if (arguments.size > paramCount){
            thrower.RuntimeException("Insufficient parameters!")
        }
        for (names in functions.keys){
            env.define(names,functions[names])
        }
        return env
    }
    override fun toString(): String {
        return "NativeClass"
    }
}