package net.mugwort.mscript.core.ast.core


sealed class Expression {
    data class Identifier(val name: String) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "Identifier" to mutableMapOf("type" to "Identifier", "name" to name)
            )
        }
    }

    data class NumericLiteral(val value: Double?) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "NumericLiteral" to mutableMapOf("type" to "NumericLiteral", "value" to value)
            )
        }
    }

    data class StringLiteral(val value: String?) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "StringLiteral" to mutableMapOf("type" to "StringLiteral", "value" to value)
            )
        }
    }

    data class ObjectLiteral(val value: Any?) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ObjectLiteral" to mutableMapOf("type" to "ObjectLiteral", "value" to value)
            )
        }
    }

    data class BooleanLiteral(val value: Boolean?) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "BooleanLiteral" to mutableMapOf("type" to "BooleanLiteral", "value" to value)
            )
        }
    }

    data object NullLiteral : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf("NullLiteral" to mutableMapOf("type" to "NullLiteral"))
        }
    }


    data class BinaryExpression(val operator: Any?, val left: Expression, val right: Expression) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "BinaryExpression" to mutableMapOf(
                    "type" to "BinaryExpression",
                    "operator" to operator,
                    "left" to left.toMap(),
                    "right" to right.toMap()
                )
            )
        }
    }

    data class GroupExpression(val expr: Expression) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "GroupExpression" to mutableMapOf(
                    "type" to "GroupExpression",
                    "group" to expr.toMap(),
                )
            )
        }
    }

    data class UnaryExpression(val operator: String, val argument: Expression) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "UnaryExpression" to mutableMapOf(
                    "type" to "UnaryExpression",
                    "operator" to operator,
                    "argument" to argument.toMap()
                )
            )
        }
    }

    data class MemberExpression(val objectExpression: Expression, val property: Expression, val computed: Boolean) :
        Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "MemberExpression" to mutableMapOf(
                    "type" to "MemberExpression",
                    "objectExpression" to objectExpression.toMap(),
                    "property" to property.toMap(),
                    "computed" to computed
                )
            )
        }
    }

    data class CallExpression(val caller: Identifier, val arguments: List<Expression>) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "CallExpression" to mutableMapOf(
                    "type" to "CallExpression",
                    "caller" to caller.toMap(),
                    "arguments" to arguments.map { it.toMap() }
                )
            )
        }
    }

    data class AssignmentExpression(val left: Expression, val operator: String, val right: Expression) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "AssignmentExpression" to mutableMapOf(
                    "type" to "AssignmentExpression",
                    "left" to left.toMap(),
                    "operator" to operator,
                    "right" to right.toMap()
                )
            )
        }
    }

    data class LogicalExpression(val operator: String, val left: Expression, val right: Expression) : Expression() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "LogicalExpression" to mutableMapOf(
                    "type" to "LogicalExpression",
                    "operator" to operator,
                    "left" to left.toMap(),
                    "right" to right.toMap()
                )
            )
        }
    }

    abstract fun toMap(): MutableMap<Any?, Any?>
}