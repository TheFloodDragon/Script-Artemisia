package net.mugwort.mscript.runtime.function

import net.mugwort.mscript.runtime.ICallable

class Function : ICallable {
    override val paramCount: Int
        get() = 1

    override fun call(arguments: List<Any?>) : Any? {
        return null
    }

    override fun bind(side: Any?) {

    }
}