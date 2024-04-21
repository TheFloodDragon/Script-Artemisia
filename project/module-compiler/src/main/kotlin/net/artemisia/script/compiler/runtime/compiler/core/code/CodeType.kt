package net.artemisia.script.compiler.runtime.compiler.core.code

enum class CodeType(val byte: Byte) {

    //操作符
    IMPORT(0x0A),
    LOADDATA(0x1A),
    SAVEVAR(0x2A),
    SAVECNT(0x3A),
    CALL(0x4A),
    GETPRAMS(0x5A),
    PUSH(0x6A),
    BLOCK(0x7A),

    //IF
    LOADIFOP(0x01B),
    CMP(0x0B),

    //运算符
    ADD(0x00),
    MIN(0x01),
    MIT(0x02),
    DIV(0x03),
    MOD(0x04),
    AGT(0x05)
}