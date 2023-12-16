package net.mugwort.mscript.core.ast.token

import net.mugwort.mscript.core.util.baseJson

class Location(
    var start : Position,
    var end : Position
) {
    class Position(var line : Int, var column : Int){
        override fun toString(): String {
            return baseJson.toJson(mapOf("line" to line,"column" to column))
        }
    }

    override fun toString(): String {
        return baseJson.toJson(mapOf("start" to start,"end" to end))
    }
 }