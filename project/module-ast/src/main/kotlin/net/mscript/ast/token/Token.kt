package net.mscript.ast.token

import net.mscript.utils.JsonUtils

class Token(var type: TokenType, var value: String) {

    override fun toString(): String {
        return JsonUtils.toJson(mapOf("type" to type, "value" to value))!!
    }
}