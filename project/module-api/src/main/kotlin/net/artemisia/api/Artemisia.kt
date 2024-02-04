package net.artemisia.api

object Artemisia {
    private val ScriptBus : IScriptBus = IScriptBus()
    fun getBus(): IScriptBus {
        return ScriptBus
    }

}