package net.artemisia.script.compiler.runtime.compiler.initialize.objects.other

class PathObject {
    // M(a/b/c,Object)
    fun setPath(path : String,loads : ArrayList<String>): String {
        return "M($path,${loads.joinToString(separator = ",")})"
    }
    fun gen(value : String): List<Byte>{
         return value.toByteArray().toList()
    }


}