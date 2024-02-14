package net.artemisia.api.plugins

import com.google.gson.Gson
import net.artemisia.api.Console
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile


class PluginManager {

    private val plugins: MutableMap<String, Double> = mutableMapOf()

    data class Dependency(
        val id: String,
        val version: String
    )

    data class PluginInfo(
        val main: String,
        val name: String,
        val version: String,
        val author: String,
        val dependencies: List<Dependency>,
        val description: String
    )

    fun addPlugin(id: String, version: Double) {
        plugins[id] = version
    }

    fun getPlugins(): MutableMap<String, Double> {
        return plugins
    }

    private fun getResource(jar: String, file: String): String? {
        try {
            val jarFile = JarFile(jar)
            val entry = jarFile.getEntry(file)
            if (entry != null) {
                val inputStream: InputStream = jarFile.getInputStream(entry)
                val content = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()
                return content
            } else {
                println("在Jar包中找不到指定的文件：$file")
            }
            jarFile.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun pluginInfo(info: String): PluginInfo {
        val gson = Gson()
        return gson.fromJson(info, PluginInfo::class.java)
    }

    fun loadPlugins(dir: File) {
        for (file in dir.listFiles()!!) {
            if (file.extension == "jar") {
                val info = getResource(file.path, "plugin.json")?.let { pluginInfo(it) }
                Console.info("LoadPlugin ['${info?.name}'] of version ${info?.version}")
                val url = URL("file:///${file.absoluteFile}")
                try {
                    val classLoader = URLClassLoader(arrayOf(url))
                    val pluginClassName = info?.main
                    if (info != null) {
                        plugins[info.name] = info.version.toDouble()
                    }
                    for (depend in info?.dependencies!!) {
                        val depends = depends(depend)
                        if (!depends) {
                            throw Exception("Plugin need ${depend.id} version ${depend.version} but it is ${plugins[depend.id]}")
                        }
                    }
                    val pluginClass = classLoader.loadClass(pluginClassName)
                    val pluginInstance = pluginClass.newInstance()
                    if (pluginInstance is ArtemisiaPlugin) {
                        pluginInstance.initialize()
                    } else {
                        println("插件主类未实现ArtemisiaPlugin接口")
                    }
                } catch (e: Exception) {
                    Console.err("CantLoad ['${info?.name}'] is update?")
                    e.printStackTrace()
                }

            }
        }
    }

    private fun depends(depends: Dependency): Boolean {
        val id = depends.id
        val version = splitVersion(depends.version)
        if (!plugins.keys.contains(id)) {
            throw Exception("Plugin depend on $id")
        } else {
            val pv = plugins[id]
            val dv = version[1].toDouble()
            if (pv != null) {

                when (version[0]) {
                    ">=" -> return dv >= pv
                    "<=" -> return dv <= pv
                    "=" -> return dv == pv
                    ">" -> return dv > pv
                    "<" -> return dv < pv
                }
            }
        }
        return false
    }

    private fun splitVersion(input: String): List<String> {
        val regex = Regex("([>=]+|<=?|>=?|==?)?\\s*([0-9]+(?:\\.[0-9]+)?)")
        return regex.findAll(input)
            .flatMap { it.groupValues.drop(1) }
            .toList()
            .let {
                if (it.size == 1) listOf("=", it[0])
                else it
            }
    }

}