package net.mugwort.artemisia.application.plugin

import net.mugwort.artemisia.api.plugins.PluginManager
import java.io.File

class Plugins {
    val manager = PluginManager()
    fun init(){
        val file = File("plugins")
        if (!file.exists()){
            file.mkdir()
        }
        manager.loadPlugins(file)
    }

}