package compiler.runtime.compiler.core.data

import common.token.TokenType

enum class DataType(val byte : Byte) {

    INT(0x00),
    FLOAT(0x01),
    DOUBLE(0x02),
    STRING(0x03),
    IDENTIFIER(0x04),
    PATH(0x05);

    companion object{
 
        fun fromByte(id: Byte): DataType? {
            return DataType.entries.firstOrNull { it.byte == id }
        }
    }
}