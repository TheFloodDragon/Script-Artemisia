package net.artemisia.script.compiler

import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import java.io.File

class Compiler(val file : File, val outputAsm : Boolean)  {
    private val program = ASMCompiler(file).compiler()
    private val cache = "__artemisia-cache__"
    private val bytes = ArrayList<Byte>()

    private val id = file.nameWithoutExtension
    private val filepath = file.parentFile.path
    private var ext = ".asp"

    init {

        bytes.addAll(byteArrayOf(
            0xAC.toByte(),
            0xCB.toByte(),
            0xBA.toByte(),
            0xBA.toByte(),
            0x00,
            0x01,
            0x00,
            0x00
            ).toList())
        if (outputAsm){
            write()
        }
        compiler()
        ext = ".apc"
        write(true)

    }

    fun write(byte : Boolean = false){
        val file = File(filepath + File.separator + cache + File.separator + id + ext)
        if (!file.exists()){
            file.parentFile.mkdirs()
            if (byte) file.writeBytes(bytes.toByteArray())  else file.writeText(program.toString())
            file.createNewFile()
        }else{
            file.delete()
            if (byte) file.writeBytes(bytes.toByteArray())  else file.writeText(program.toString())
            file.createNewFile()
        }

    }

    private fun compiler(){
        var data_index = 0
        // index | type | id_len | id | code_len | code
        for (i in program.data.getData()){
            val code : ArrayList<Byte> = arrayListOf()
            code.add(data_index.toByte())
            code.add(i.type.id)
            code.add(i.id.toByteArray().size.toByte())
            if(i.id.toIntOrNull() != null){
                code.add(i.id.toInt().toByte())


            }else{
                code.addAll(i.id.toByteArray().toList())
            }





            code.add(i.value.toString().toByteArray().size.toByte())
            code.addAll(i.value.toString().toByteArray().toList())
            bytes.addAll(code)
            data_index += 1
        }
        bytes.add(8,data_index.toByte())
        bytes.add(program.main.getData().size.toByte())
        // params_size | code | params
        for (i in program.main.getData()){
            val code : ArrayList<Byte> = arrayListOf()
            code.add(i.helpers.size.toByte())
            code.add(i.code.byte)
            for (i1 in i.helpers){
                code.add(i1.type.byte)
                if (i1.type == ASMCodeHelper.ValueType.DATA) code.add(i1.value.toString().length.toByte())
                if (i1.value.toString().toIntOrNull() != null){
                    code.add(i1.value.toString().toInt().toByte())
                }else{
                    code.addAll(i1.value.toString().toByteArray().toList())
                }

            }
            bytes.addAll(code)
        }



    }





}