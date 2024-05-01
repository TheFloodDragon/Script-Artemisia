package vm.runtime.interpreter.core

class Environment(val parent : Environment? = null) {
    val env : MutableMap<String,Any?> = mutableMapOf()

    fun define(id : String,value : Any?){
        env[id] = value
    }

    fun search(id : String): Any? {
        return env[id] ?: parent?.env?.get(id)
    }

    override fun toString(): String {
        return env.toString()
    }
}