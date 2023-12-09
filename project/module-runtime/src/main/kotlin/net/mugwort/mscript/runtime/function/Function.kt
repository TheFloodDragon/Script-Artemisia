package net.mugwort.mscript.runtime.function

import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.runtime.Environment
import net.mugwort.mscript.runtime.ICallable


class Function(
    private val declaration: Statement.FunctionDeclaration,
    private val parent: Environment
) : ICallable {

    private var site : Any? = null

    override val paramCount: Int
        get() = declaration.params.count()

    override fun call(arguments: List<Any?>) : Any? {
        val env = Environment(parent)
        env.defind("self",this)
        if (site != null) {
            env.defind("this", site)
            unBind()
        }

        val size = if (paramCount >= 0) paramCount else arguments.size
        for (i in 0 until size) {
            env.defind(declaration.params[i].declarations.id.name, arguments[i])
        }
        return null
    }

    override fun bind(site: Any?) {

    }
    fun unBind(){
        this.site = null
    }
}