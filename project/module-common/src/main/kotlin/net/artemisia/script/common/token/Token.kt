package net.artemisia.script.common.token

import net.artemisia.script.common.location.Location


class Token(var type: TokenType, var value: String, var location: Location) {

    override fun toString(): String {
        return mapOf(
                "type" to type,
                "value" to value,
                "location" to location
        ).toString()
    }

}