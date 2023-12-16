package net.mugwort.mscript.compiler.interpreter.statements.classes.core

import net.mugwort.mscript.compiler.interpreter.statements.function.core.NativeFunction
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower

class NativeClass(private val functions:MutableMap<String, NativeFunction>) : ICallable{
    private val env : Environment = Environment()

    override val paramCount: Int
        get() = 0

    override fun call(arguments: List<Any?>): Any? {
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