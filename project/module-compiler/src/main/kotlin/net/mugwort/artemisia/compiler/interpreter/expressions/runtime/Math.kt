package net.mugwort.artemisia.compiler.interpreter.expressions.runtime

class Math(private val left: Any?, private val op: Any?, private val right: Any?) {
    fun get(): Any {
        if (left is String) {
            return left + right
        } else {
            return when (op) {
                "+" -> (left.toString().toDouble() + right.toString().toDouble())
                "-" -> (left.toString().toDouble() - right.toString().toDouble())
                "/" -> (left.toString().toDouble() / right.toString().toDouble())
                "%" -> (left.toString().toDouble() % right.toString().toDouble())
                "*" -> (left.toString().toDouble() * right.toString().toDouble())
                else -> {
                    return 0
                }
            }
        }
    }
}