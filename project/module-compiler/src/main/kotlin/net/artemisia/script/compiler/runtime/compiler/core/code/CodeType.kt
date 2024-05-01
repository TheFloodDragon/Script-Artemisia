package compiler.runtime.compiler.core.code

enum class CodeType(val byte: Byte) {

    //操作符
    IMPORT(0x0A),

    LOADDATA(0x1A),
    SAVEVAR(0x2A),
    SAVECNT(0x3A),
    CALL(0x4A),
    RET(0x5A),
    PUSH(0x6A),
    BLOCK(0x7A),

    //IF
    LOADIFOP(0x1B),
    CMP(0x0B),

    //LOADIDENTIFIER(0x2B),
    LOADATTRIBUTE(0x3B),
    CALLARRTIBUTE(0x4B),


    SETPARAMS(0x5B),
    SAVEPARAMS(0x0C),
    SAVEPARAMSCNT(0x1C),
    NEWMETHOD(0x2C),
    INVKOETYPE(0x6B),
    CLS(0x3C),

    //运算符

    ADD(0x00),
    MIN(0x01),
    MIT(0x02),
    DIV(0x03),
    MOD(0x04),
    AGT(0x05);

    companion object{
 
        fun fromByte(id: Byte): CodeType? {
            return CodeType.entries.firstOrNull { it.byte == id }
        }
    }
}