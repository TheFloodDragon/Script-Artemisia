package net.artemisia.script.compiler.runtime.compiler.types

enum class ASMCode(val id : String,val byte : Byte) {
    //操作符
    LD("ld",0x1A),
    LP("lp",0x2A),
    LC("lc",0x3A),
    SP("sp",0x4A),
    PUSH("push",0x5A),
    IV("iv",0x6A),
    IL("il",0x7A),
    MOVL("movl",0x1B),
    CALL("call",0x2B),
    IM("im",0x3B),
    END("end",0x4B),
    RET("ret",0x5B),
    //运算符
    ADD("add",0x01),
    MIN("min",0x02),
    MUL("mul",0x03),
    DIV("div",0x04),
    MOD("mod",0x05)
}