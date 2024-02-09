package net.artemisia.vm

import net.artemisia.runtime.compiler.objects.module.TypeObject
import net.artemisia.runtime.compiler.objects.module.VisitorObject
import java.nio.ByteBuffer
import kotlin.text.Charsets.UTF_8


class Parser(private val code : ByteArray) {
    private var index : Int = 8
    private val magic : ByteArray = code.sliceArray(0 until 4)
    val version = code.sliceArray(4 until 8)
    val stacksize = run {
        val size = code[index].toInt()
        val value =  code[index + size]
        advance(size)
        value
    }
    val pool = run {
        advance()
        val poolSize = look().toInt()
        advance()
        val pool = ConstantPool()
        val array = spiltCode(poolSize,false)
        var i = 0
        while (true){
            if (i >= poolSize) break
            val index = array[i].toInt()
            i += 1
            val t = array[i]
            i+= 1
            val len = array[i]
            i += 1
            val v = array.sliceArray(i until i + len)
            i += len
            val type = when (t) {
                TypeObject.Type.NUMBER.id -> {
                    "Number"
                }
                TypeObject.Type.BOOLEAN.id -> {
                    "Boolean"
                }
                TypeObject.Type.IDENTIFIER.id -> {
                    "Identifier"
                }
                TypeObject.Type.STRING.id -> {
                    "String"
                }
                else -> {
                    "Void"
                }
            }
            val value = when(type){
                "Number" -> {
                    try {
                        ByteBuffer.wrap(v).getDouble()
                    }catch (e : Exception){
                        v[0].toInt()
                    }
                }
                "Boolean" -> v[0] == 0x01.toByte()
                "Identifier" -> v.toString(UTF_8)
                "String" -> {

                    v.toString(UTF_8)
                }
                else -> {
                    ""
                }
            }
            pool.add(index, arrayListOf(type,value))

        }
        index += poolSize
        pool
    }
    val codes = getCode(code)
    private fun getCode(code: ByteArray): MutableMap<Int, ArrayList<String>> {
        fun toByte(i : Int): Byte {
            return i.toByte()
        }
        val len = look()
        advance()
        val codes = spiltCode(len.toInt())
        val codeMap : MutableMap<Int,ArrayList<String>> = mutableMapOf()
        var i = 0
        var mapIndex = 0
        while (true){
            if (i >= len) break
            val type = when(codes[i]){
                toByte(0x4A) -> "LoadModule"
                toByte(0x2A) -> "Call"
                toByte(0x6A) -> "EventListener"
                toByte(0x7A) -> "CreateFunction"
                toByte(0x22) -> "Push"
                toByte(0x11) -> "SetVariable"
                toByte(0x12) -> "SaveItem"
                toByte(0x13) -> "SaveConstant"

                toByte(0x14) -> "InvokeType"
                else -> { "" }
            }
            i += 1
            val index = codes[i]
            i += 1
            codeMap[mapIndex] = arrayListOf(type,index.toString())
            mapIndex += 1
        }
        return codeMap
    }
    val functions = run {
        var iSize = 0
        val oSize = look().toInt()
        val funcMap : MutableMap<Int,ArrayList<Any>> = mutableMapOf()
        if (oSize == 0) {
            return@run funcMap
        }
        advance()
        while (true){
            if (iSize >= oSize) break
            val index = look()
            advance()
            val id = getIdentifier()
            val argsSize = look().toInt()
            advance()
            var argsISize = 0
            val args : MutableMap<Int,ArrayList<Any>> = mutableMapOf()
            while (true){
                if (argsISize >= argsSize) break
                advance()
                val ids = getIdentifier()
                val type = getType()
                if (type.size >= 2){
                    val value = type[1]
                    args[argsISize] = arrayListOf(ids,type,value)
                }else{
                    args[argsISize] = arrayListOf(ids,type)
                }
                argsISize += 1
            }
            val code = getCode(spiltCode(look().toInt(),false))
            val returnType = getType()
            funcMap[index.toInt()] = arrayListOf(id,args,code,returnType)
            iSize += 1
        }

        funcMap
    }

    val listeners = run {
        advance()
        var i = 0
        val size = look().toInt()
        advance()
        val listeners : MutableMap<Int,ArrayList<Any>> = mutableMapOf()
        if (size == 0) {
            return@run listeners
        }
        while (true){
            if (i >= size) break
            val index = look()
            advance()
            val id = getIdentifier()
            val argsSize = look().toInt()
            advance()
            var argsISize = 0
            val args : MutableMap<Int,ArrayList<Any>> = mutableMapOf()

            while (true){
                if (argsISize >= argsSize) break
                advance()
                val ids = getIdentifier()

                val type = getType()

                if (type.size >= 2){
                    val value = type[1]
                    args[argsISize] = arrayListOf(ids,type,value)
                }else{
                    args[argsISize] = arrayListOf(ids,type)
                }
                argsISize += 1
            }

            val code = getCode(spiltCode(look().toInt(),false))
            listeners[index.toInt()] = arrayListOf(id,args,code)
            i += 1
        }

        listeners
    }

    val visitors = run {
        val size = look().toInt()
        val visitorMap : MutableMap<Int,ArrayList<Any>> = mutableMapOf()
        if(size == 0){
            return@run visitorMap
        }
        advance()
        var i = 0
        while (true){
            if (size <= i) break
            val index = look().toInt()
            advance()
            val visitType = when(look()){
                VisitorObject.VisitorType.ALREADY.i -> "already"
                VisitorObject.VisitorType.PROTECTED.i -> "protected"
                VisitorObject.VisitorType.PRIVATE.i -> "private"
                else -> "public"
            }
            advance()
            val obj = when(look()){
                VisitorObject.VisitObject.FUNCTION.i -> "function"
                VisitorObject.VisitObject.VARIABLE.i -> "variable"
                else -> "class"
            }
            advance()
            val vIndex = look().toInt()
            val value = when(obj) {
                "function" -> functions[vIndex] as ArrayList<*>
                "variable" -> pool.pool[vIndex] as ArrayList<*>
                else -> {
                    arrayListOf()
                }
            }
            advance()
            i += 4
            visitorMap[index] = arrayListOf(visitType,obj,value)
        }

        visitorMap
    }



    fun getType(): ArrayList<Any> {
        advance()
        val type = when (look()) {
            TypeObject.Type.NUMBER.id -> {
                "Number"
            }
            TypeObject.Type.BOOLEAN.id -> {
                "Boolean"
            }
            TypeObject.Type.IDENTIFIER.id -> {
                "Identifier"
            }
            TypeObject.Type.STRING.id -> {
                "String"
            }
            else -> {
                "Void"
            }
        }
        advance()
        if (look().toInt() != 0){
            val value = when(type){
                "Identifier" -> getIdentifier()
                "Number" -> getNumber()
                "Boolean" -> getBoolean()
                "String" -> getString()
                else -> {
                    "(void)"
                }
            }
            return arrayListOf(type,value)
        }

        return arrayListOf(type)
    }
    private fun getString() : String{
        val len = look()
        advance()
        return spiltCode(len.toInt()).toString(UTF_8)
    }

    private fun getBoolean() : Boolean{
        val bool = look()
        advance()
        return bool == 0x01.toByte()
    }
    private fun getNumber() : Number{
        val len = look()
        advance()
        val value = spiltCode(len.toInt())
        val number =  try {
            ByteBuffer.wrap(value).getDouble()
        }catch (e : Exception){
            value[0].toInt()
        }
        return number
    }

    private fun getIdentifier(): String {
        val len = look().toInt()
        advance()
        return spiltCode(len).toString(UTF_8)
    }
    private fun look(): Byte {
        return code[index]
    }

    private fun advance(value : Int = 1): Byte {
        if (!isEnd()) index += value
        return code[index]
    }

    private fun spiltCode(len: Int,jump : Boolean = true): ByteArray {
        val bytes = code.sliceArray(index until index + len)
        if (jump) advance(len)
        return bytes
    }

    private fun isEnd(): Boolean {
        return index >= code.size
    }

}