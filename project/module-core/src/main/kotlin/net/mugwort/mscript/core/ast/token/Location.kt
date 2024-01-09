package net.mugwort.mscript.core.ast.token

class Location(
    var line : Int,
    var column : Int
) {

    fun toMap(): Map<String, Int> {
        return mapOf("line" to line, "column" to column)
    }

    override fun toString(): String {
        return mapOf("line" to line, "column" to column).toString()
    }
}