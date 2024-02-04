package net.artemisia.core.ast.token

import net.artemisia.core.util.baseJson

class Token(var type: TokenType, var value: String, var location: Location) {

    override fun toString(): String {
        return baseJson.toJson(mapOf(
            "type" to type,
            "value" to value,
            "location" to location
        ))!!
    }

}