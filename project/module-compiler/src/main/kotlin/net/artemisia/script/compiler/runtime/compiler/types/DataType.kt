package net.artemisia.script.compiler.runtime.compiler.types

enum class DataType(val datatype: String,val id : Byte) {

    // int
    DI("di",0x00),
    //float
    DF("df",0x01),
    //double
    DD("dd",0x02),
    //string
    DS("ds",0x03),
    //identifier
    DT("dt",0x04),
    //Module
    DM("dm",0x05)


}