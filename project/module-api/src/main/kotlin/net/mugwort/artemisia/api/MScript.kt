package net.mugwort.artemisia.api

object MScript {
    private val ScriptBus : IScriptBus = IScriptBus()
    fun getBus(): IScriptBus {
        return ScriptBus
    }
    fun <E : Any> register(){


    }
}