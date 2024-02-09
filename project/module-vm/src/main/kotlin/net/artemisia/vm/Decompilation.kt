package net.artemisia.vm

import java.io.File

class Decompilation(val file : File) {
    private val parser = Parser(file.readBytes())
    private val pool = parser.pool.pool

    fun output(){
        main()
        pool()
        code()
        function()
        event()
        visitor()






    }

    private fun main(){
        println("***********************[ Main ]************************")
        println("version : ${parser.version.toList()}")
        println("stackSize: ${parser.stacksize}")
    }
    private fun pool(){
        println("*******************[ Constant Pool ]*******************")
        for (i in pool.keys){
            println("$i - ${pool[i]!![0]} : ${pool[i]!![1]}")
        }
    }
    private fun code(){
        println("***********************[ Code ]************************")
        for (i in parser.codes.keys) {
            println(
                "$i - ${parser.codes[i]!![0]} : ${parser.codes[i]!![1]} (" +
                        "${
                            if (parser.codes[i]!![0] == "CreateFunction") {
                                parser.functions[parser.codes[i]!![1].toInt()]
                            } else if(parser.codes[i]!![0] == "InvokeType"){
                                if(pool[parser.codes[i]!![1].toInt()] == null){
                                    when(parser.codes[i]!![1].toInt()){
                                        0x01 -> "String"
                                        0x02 -> "Number"
                                        0x03 -> "Boolean"
                                        else -> "Object"
                                    }
                                }else{
                                    pool[parser.codes[i]!![1].toInt()]
                                }
                            }
                            else {
                                pool[parser.codes[i]!![1].toInt()]
                            }
                        }" +
                        ")"
            )
        }
    }
    private fun function(){
        println("********************[ Functions ]**********************")
        val func = parser.functions
        for (i in func.keys){
            println("$i -")
            println("   id: ${func[i]!![0]}")
            println("   args: ")
            val args = func[i]!![1] as MutableMap<*, *>
            for (b in func.keys){
                if (args[0] != null){
                    println("       $b - ${
                        (args[b] as ArrayList<*>)[0]
                    } : ${
                        (args[b] as ArrayList<*>)[1]
                    }")
                }
            }

            println("   codes:")
            val codes = func[i]!![2] as MutableMap<*, *>
            for (c in codes.keys){
                println("       $c - ${(codes[c] as ArrayList<*>)[0]} : ${(codes[c] as ArrayList<*>)[1]} (${if ((codes[c] as ArrayList<*>)[0] == "CreateFunction") parser.functions[(codes[c] as ArrayList<*>)[1].toString().toInt()] else pool[(codes[c] as ArrayList<*>)[1].toString().toInt()]})")
            }
            println("   return: ${func[i]!![3]}")
        }

    }
    private fun event(){
        println("*********************[ Event ]************************")
        val event = parser.listeners

        for (i in event.keys){
            println("$i -")
            println("   id: ${event[i]!![0]}")
            println("   listeners: ")
            val listen = event[i]!![1] as MutableMap<*, *>
            for (b in event.keys){
                if (listen[0] != null){
                    println("       $b - ${
                        (listen[b] as ArrayList<*>)[0]
                    } : ${
                        (listen[b] as ArrayList<*>)[1]
                    }")
                }
            }

            println("   codes:")
            val codes = event[i]!![2] as MutableMap<*, *>
            for (c in codes.keys){
                println("       $c - ${(codes[c] as ArrayList<*>)[0]} : ${(codes[c] as ArrayList<*>)[1]} (${if ((codes[c] as ArrayList<*>)[0] == "CreateFunction") parser.functions[(codes[c] as ArrayList<*>)[1].toString().toInt()] else pool[(codes[c] as ArrayList<*>)[1].toString().toInt()]})")
            }
        }

    }
    private fun visitor(){
        println("********************[ Visitors ]**********************")
        val visitor = parser.visitors
        for (i in visitor.keys){
            println(" $i : ${visitor[i]!![0]} - ${visitor[i]!![1]} : ${visitor[i]!![2]}")
        }
    }
}