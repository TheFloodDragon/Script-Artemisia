package net.artemisia.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object JsonUtils {
    fun toJson(any: Any): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(any)

        val jsonElement = JsonParser.parseString(jsonString)
        return gson.toJson(jsonElement)
    }

    object Unicode {

        private fun Int.toHexString(): String = Integer.toHexString(this)

        private fun encode(char: Char) = "\\u${char.code.toHexString()}"

        fun encode(text: String) = text
            .toCharArray().joinToString(separator = "", truncated = "") { encode(it) }

        fun decode(encodeText: String): String {
            fun decode1(unicode: String) = unicode.toInt(16).toChar()
            val unicodes = encodeText.split("\\u").mapNotNull { if (it.isNotBlank()) decode1(it) else null }
            return String(unicodes.toCharArray())
        }
    }
}