package net.mugwort.artemisia.runtime.compiler.objects

import net.mugwort.artemisia.runtime.compiler.ConstantPool
import net.mugwort.artemisia.runtime.compiler.Object

/*
    <魔数>
    <版本>
    <常量池数据总长度>
    <常量池>
        | <索引> <类型> <长度> <值>
    <主代码区数据长度>
    <主代码区>
        | <代码索引> <常量池数据索引>
    <方法区数据长度>
    <方法区>
        | <方法类型|type> <名称|id> <参数|arg>
    <文件信息区数据长度>
    <文件地址>
    <编译日期数据长度>
    <编译日期>
    <编译类型长度>
    <编译类型>
    <30个0x00占位符>
*/


class ModuleObject(
    private val version : ByteArray,
    private val constants : ConstantPool,
    private val code : ArrayList<CodeObject>,
    private val functions : ArrayList<FunctionObject>,
    private val visitors : ArrayList<VisitorObject>,
    private val file : ByteArray,
    private val date : ByteArray

) : Object() {
    override fun toByte(): ByteArray {
        val array : ArrayList<Byte> = arrayListOf()
        array.addAll(arrayListOf(
            (0xAE).toByte(),(0xBE).toByte(),(0xEE).toByte(),(0xEE).toByte()
        ))
        array.addAll(version.toList())
        array.add(constants.getPool().size.toByte())
        for (key in constants.getPool().keys){
            array.add(key.toByte())
            val value = constants.getPool()[key]!!
            array.add(value[0])
            array.add(value[1])
            array.addAll(value.subList(2,value.size))
        }

        array.add(code.size.toByte())
        for (i in code){
            array.addAll(i.toByte().toList())
        }
        array.add(functions.size.toByte())
        for (i in functions){
            array.add(functions.indexOf(i).toByte())
            array.addAll(i.toByte().toList())
        }
        array.add(visitors.size.toByte())
        for (i in visitors){
            array.add(visitors.indexOf(i).toByte())
            array.addAll(i.toByte().toList())
        }
        array.add(file.toList().size.toByte())
        array.addAll(file.toList())
        array.add(date.size.toByte())
        array.addAll(date.toList())
        array.add("<M>".toByteArray().size.toByte())
        array.addAll("<M>".toByteArray().toList())
        array.add(0x20)
        for (i in 0 until 30){
            array.add(0x00)
        }


        return array.toByteArray()
    }

}