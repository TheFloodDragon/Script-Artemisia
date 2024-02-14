package net.artemisia.api.types

open class Object {
    class Identifier(id: kotlin.String, default: kotlin.Boolean = true) : Object()
    class Number(number: kotlin.Number? = null) : Object()
    class OBJ(obj: Any? = null) : Object()
    class String(string: kotlin.String? = null) : Object()
    class Boolean(boolean: kotlin.Boolean? = null) : Object()
}