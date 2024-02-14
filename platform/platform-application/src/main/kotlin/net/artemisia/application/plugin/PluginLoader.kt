package net.artemisia.application.plugin

import net.artemisia.api.plugins.PluginManager
import java.io.File

class PluginLoader {
    val manager = PluginManager()
    fun init() {
        val file = File("plugins")
        if (!file.exists()) {
            file.mkdir()
        }
        manager.loadPlugins(file)
    }

}