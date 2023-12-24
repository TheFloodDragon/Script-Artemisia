package net.mugwort.mscript.api

object MScript {
    private val ScriptBus : IScriptBus = IScriptBus()
    fun getBus(): IScriptBus {
        return this.ScriptBus
    }
    fun <E : Any> register(){


    }
}