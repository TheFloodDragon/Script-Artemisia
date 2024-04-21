package net.artemisia.script.compiler.runtime.compiler.core.data

enum class DataType(val byte : Byte) {

    INT(0x00),
    FLOAT(0x01),
    DOUBLE(0x02),
    STRING(0x03),
    IDENTIFIER(0x04),
    PATH(0x05)


}