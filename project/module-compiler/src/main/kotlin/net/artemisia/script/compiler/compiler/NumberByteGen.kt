package net.artemisia.script.compiler.compiler

class NumberByteGen(val i : Number) {
    fun parser(): ByteArray {
        when (i){
            is Int -> {
                return IntObject(i).parser()
            }
            else -> {
                return DoubleObject(i as Double).parser()
            }
        }


    }


}