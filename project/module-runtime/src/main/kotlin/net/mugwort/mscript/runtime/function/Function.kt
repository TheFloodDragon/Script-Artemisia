package net.mugwort.mscript.runtime.function

import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.ICallable


class Function(private val declaration: Statement.FunctionDeclaration,private val parent: Environment,private val returns : Any?) : ICallable {
    private var site: Any? = null
    override val paramCount: Int = declaration.params.count()
    override fun call(arguments: List<Any?>): Any? {
        val env = Environment(parent)
        env.define("self", this)
        if (site != null) {
            env.define("this", site)
            unBind()
        }

        val size = if (paramCount >= 0) paramCount else arguments.size
        for (i in 0 until size) {
            env.define(declaration.params[i].declarations.id.name, arguments[i])
        }
        if (returns != null){
            return returns
        }
        return null
    }



    override fun bind(site: Any?) {
        this.site = site
    }

    private fun unBind() {
        this.site = null
    }
}