package net.mugwort.mscript.compiler.runtime

import net.mugwort.mscript.runtime.ICallable
import net.mugwort.mscript.runtime.expection.thrower


typealias Caller = (Any?, List<Any?>) -> Any?

class NativeFunction(arity: Int, private val caller: Caller) : ICallable {
    private var site: Any? = null

    override val paramCount: Int = arity
    override fun call(arguments: List<Any?>): Any? {
        if (arguments.size > paramCount){
            thrower.RuntimeException("Insufficient parameters!")
        }
        val site = site
        unBind()
        return caller(site, arguments)
    }

    override fun bind(site: Any?) {
        this.site = site
    }

    private fun unBind() {
        this.site = null
    }
}

