package net.mugwort.mscript.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object JsonUtils {
    fun toJson(any: Any): String? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(any)

        val jsonElement = JsonParser.parseString(jsonString)
        return gson.toJson(jsonElement)
    }
}