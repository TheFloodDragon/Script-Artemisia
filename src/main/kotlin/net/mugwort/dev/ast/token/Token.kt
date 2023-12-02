package net.mugwort.dev.ast.token

import net.mugwort.dev.utils.JsonUtils

class Token(var type: TokenType, var value: String) {

    override fun toString(): String {
        return JsonUtils.toJson(mapOf("type" to type, "value" to value))!!
    }
}