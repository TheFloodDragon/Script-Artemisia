package net.mugwort.mscript.ast.core


sealed class Expression {
    data class Identifier(val name: String) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "Identifier" to mutableMapOf("type" to "Identifier", "name" to name)
            )
        }
    }
    data class NumericLiteral(val value: Double?) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "NumericLiteral" to mutableMapOf("type" to "NumericLiteral", "value" to value)
            )
        }
    }
    data class StringLiteral(val value: String?) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "StringLiteral" to mutableMapOf("type" to "StringLiteral", "value" to value)
            )
        }
    }
    data class ObjectLiteral(val value: Any?) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ObjectLiteral" to mutableMapOf("type" to "ObjectLiteral", "value" to value)
            )
        }
    }
    data class BooleanLiteral(val value: Boolean?) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "BooleanLiteral" to mutableMapOf("type" to "BooleanLiteral", "value" to value)
            )
        }
    }
    data object NullLiteral : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf("NullLiteral" to mutableMapOf("type" to "NullLiteral"))
        }
    }


    data class BinaryExpression(val operator: Any?, val left: Expression, val right: Expression) : Expression(){
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

    data class BinaryParentExpression(val binary: Expression) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "BinaryParentExpression" to mutableMapOf(
                    "type" to "BinaryParentExpression",
                    "binary" to binary,
                )
            )
        }
    }

    data class UnaryExpression(val operator: String, val argument: Expression) : Expression(){
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
    data class MemberExpression(val objectExpression: Expression, val property: Expression, val computed: Boolean) : Expression(){
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
    data class CallExpression(val callee: Expression, val arguments: List<Expression>) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "CallExpression" to mutableMapOf(
                    "type" to "CallExpression",
                    "callee" to callee.toMap(),
                    "arguments" to arguments.map { it.toMap() }
                )
            )
        }
    }
    data class NewExpression(val callee: Expression, val arguments: List<Expression>) : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "NewExpression" to mutableMapOf(
                    "type" to "NewExpression",
                    "callee" to callee.toMap(),
                    "arguments" to arguments.map { it.toMap() }
                )
            )
        }
    }
    data class AssignmentExpression(val left: Expression, val operator: String, val right: Expression) : Expression(){
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
    data class LogicalExpression(val operator: String, val left: Expression, val right: Expression) : Expression(){
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
    data object ThisExpression : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf("ThisExpression" to mutableMapOf("type" to "ThisExpression"))
        }
    }
    data object Super : Expression(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf("Super" to mutableMapOf("type" to "Super"))
        }
    }
    abstract fun toMap(): MutableMap<Any?, Any?>
}