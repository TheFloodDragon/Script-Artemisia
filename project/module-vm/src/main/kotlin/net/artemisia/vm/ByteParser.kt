package vm

import compiler.runtime.compiler.core.code.CodePool
import compiler.runtime.compiler.core.code.CodeType
import compiler.runtime.compiler.core.data.DataPool
import compiler.runtime.compiler.core.data.DataType
import compiler.runtime.compiler.core.module.ModuleCode
import compiler.runtime.compiler.initialize.objects.other.*
import vm.runtime.CodeBlock
import java.io.File

class ByteParser(file : File) {
    private val bytecodes = file.readBytes()
    private var index = 0
    val magic = spilt(4)
    val version = spilt(4)
    val type = spilt(1)
    private val dsize = spilt(1)
    val dpool = run {
        val pool = DataPool()
        for (i in 0 until dsize[0].toInt()) {
            advance() /* advance index */
            val type = spilt(1) /* type */
            val len = spilt(1) /* len */
            val value = spilt(len[0].toInt()) /* value */
            when(val id = DataType.fromByte(type[0])!!){
                DataType.INT -> pool.push(id, IntObject().decode(value))
                DataType.FLOAT -> pool.push(id, FloatObject().decode(value))
                DataType.DOUBLE -> pool.push(id, DoubleObject().decode(value))
                DataType.STRING -> pool.push(id, StringObject().decode(value))
                DataType.IDENTIFIER -> pool.push(id, StringObject().decode(value))
                DataType.PATH -> pool.push(id, PathObject().decode(value))
            }

        }
        return@run pool
    }
    private val csize = spilt(1)[0].toInt()
    val codes = run {
        val item = ArrayList<CodeBlock>()
        for (i in 0 until csize){
            advance()
            val Btype = ModuleCode.BlockType.fromByte(spilt(1)[0])!!

            val size = spilt(1)[0]
            val len = spilt(1)[0]
            val id = StringObject().decode(spilt(len.toInt()))
            advance()

            val code = CodePool()
            while (true){
                if (StringObject().decode(look(1)) == "}") break
                advance() /* advance index */
                val type = spilt(1)
                when(val codetype = CodeType.fromByte(type[0])!!){

                    CodeType.IMPORT -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.LOADDATA -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.CALL -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.PUSH -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.BLOCK -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.LOADIFOP -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.LOADATTRIBUTE -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.CALLARRTIBUTE -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.SETPARAMS -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.INVKOETYPE -> code.push(codetype,spilt(1)[0].toInt())

                    CodeType.SAVEVAR -> code.push(codetype)
                    CodeType.SAVECNT -> code.push(codetype)
                    CodeType.RET -> code.push(codetype)
                    CodeType.CMP -> code.push(codetype)
                    CodeType.ADD -> code.push(codetype)
                    CodeType.MIN -> code.push(codetype)
                    CodeType.MIT -> code.push(codetype)
                    CodeType.DIV -> code.push(codetype)
                    CodeType.MOD -> code.push(codetype)
                    CodeType.AGT -> code.push(codetype)

                    CodeType.SAVEPARAMS -> code.push(codetype)
                    CodeType.SAVEPARAMSCNT -> code.push(codetype)
                    CodeType.NEWMETHOD -> code.push(codetype,spilt(1)[0].toInt())
                    CodeType.CLS -> code.push(codetype)
                }
            }
            item.add(CodeBlock(i,Btype,size.toInt(),id,code))
            advance()
        }
        return@run item
    }

    val module = vm.runtime.Module(version.toList(),type[0],dpool, codes)

    private fun look(range: Int): ByteArray {
        return bytecodes.sliceArray(IntRange(index,index + range - 1))
    }
    private fun advance(){
        index += 1
    }



    private fun spilt(range : Int): ByteArray {
        val count =  index + range - 1
        val result = bytecodes.sliceArray(IntRange(index,count))
        index += range
        return result
    }
}