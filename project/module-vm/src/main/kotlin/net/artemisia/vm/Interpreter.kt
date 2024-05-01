package vm

import compiler.runtime.compiler.core.code.CodePool
import compiler.runtime.compiler.core.code.CodeType
import compiler.runtime.compiler.core.code.CodeType.*
import compiler.runtime.compiler.core.module.ModuleCode
import sun.misc.Unsafe
import vm.runtime.CodeBlock
import vm.runtime.interpreter.Param
import vm.runtime.interpreter.core.Environment
import vm.runtime.interpreter.core.NativeMethods
import vm.runtime.interpreter.core.Stack
import vm.runtime.interpreter.method.Method
import vm.runtime.interpreter.method.NativeMethod

import java.io.File

class Interpreter(file : File) {
    private val parser = ByteParser(file)
    private val block = parser.module.codes
    private val pool = parser.dpool
    private val environment = Environment()
    private var debug = false
    val stack = Stack<Any?>()
    var ret : Any? = null

    init {
        NativeMethods(environment)
    }

    private val methods : CodeBlock = run {
        var a : CodeBlock? = null
        for (i in block){
            if (i.name == "methods") {
                a = i
            }
        }
        return@run a!!
    }
    private val main : CodeBlock = run {
        var a : CodeBlock? = null
        for (i in block){
            if (i.type == ModuleCode.BlockType.MODULE) {
                a = i
            }
        }
        return@run a!!
    }

    fun debug(): Interpreter {
        debug = true
        return this
    }

    fun run(block: CodeBlock = main,env: Environment = environment): Interpreter {
        loadMethods()
        var index = 0
        println("run-code: ")
        block.code.getDatas().forEach { item ->
            if (debug){
                println("   index: $index")
                println("   stack: " + stack.stack)
                println("   env: $environment")
                println("   ")
                index += 1
            }
            when (item.code){
                IMPORT -> TODO()
                LOADDATA -> TODO()
                SAVEVAR -> TODO()
                SAVECNT -> TODO()
                CALL -> {
                    when(val i = env.search(pool.search(item.index!!) as String)){
                        is NativeMethod-> {
                            val value = i.call(stack.stack)
                            stack.clear()
                            stack.push(value)
                        }
                        is Method -> {
                            val value = i.call(stack.stack)
                            stack.clear()
                            stack.push(value)
                        }
                    }
                }
                RET -> {
                    ret = stack.top()
                }
                PUSH -> {

                    stack.push(pool.search(item.index!!))
                }
                BLOCK -> TODO()
                LOADIFOP -> TODO()
                CMP -> TODO()
                LOADATTRIBUTE -> TODO()
                CALLARRTIBUTE -> TODO()
                INVKOETYPE -> TODO()
                ADD -> {
                    val top = stack.top()
                    val end = stack.pop()
                    when(top){
                        is String -> stack.push(top + end)
                        is Int -> stack.push(top + end.toString().toInt())
                        is Float -> stack.push(top + end.toString().toFloat())
                        is Double -> stack.push(top + end.toString().toDouble())
                    }
                }
                MIN -> TODO()
                MIT -> TODO()
                DIV -> TODO()
                MOD -> TODO()
                AGT -> TODO()
                CLS -> stack.clear()
                else -> {}
            }


        }
        return this
    }

    private fun loadMethods(){
        var index = 0

        var id = ""
        var type = ""
        val params = ArrayList<Param>()
        var body : CodeBlock? = null

        var isParams = false
        var paramId = ""
        var paramType = ""
        val paramPool = CodePool()

        println("methods:")
        while (index <= methods.size){
            if (debug){
                println(" index: $index")
                println("   stack: " + stack.stack)
                println("   env: $environment")
                println("   ")
            }

            if (index >= methods.size) break
            val item = methods.code.get(index)
            when(item.code){
                SETPARAMS -> {
                    paramId = pool.search(item.index!!) as String
                    isParams = true
                }
                SAVEPARAMS -> {
                    val block = CodeBlock(-1,ModuleCode.BlockType.NORMAL,paramPool.getDatas().size,"method",paramPool)
                    isParams = false
                    params.add(Param(paramId,paramType,block,false))
                    paramPool.getDatas().clear()
                }
                SAVEPARAMSCNT -> {
                    val block = CodeBlock(-1,ModuleCode.BlockType.NORMAL,paramPool.getDatas().size,"method",paramPool)
                    isParams = false
                    params.add(Param(paramId,paramType,block,true))
                    paramPool.getDatas().clear()
                }
                NEWMETHOD -> {
                    body = parser.codes[item.index!!]
                }

                LOADDATA -> {
                    if (!isParams){
                        id = pool.search(item.index!!) as String
                    }else{
                        paramPool.push(item.code,item.index)
                    }
                }
                INVKOETYPE -> if (isParams) paramType = pool.search(item.index!!) as String else type = pool.search(item.index!!) as String


                else -> if (isParams) paramPool.push(item.code,item.index)
            }
            environment.define(id,Method(params,this,body,environment))
            params.clear()
            index += 1
        }


    }



}