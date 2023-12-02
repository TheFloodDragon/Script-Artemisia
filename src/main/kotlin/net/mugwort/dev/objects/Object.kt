package net.mugwort.dev.objects

class Object {
    enum class ObjectType(private val type : kotlin.String){
        Number("NUMBER"),
        String("STRING"),
        None("NONE"),
        Boolean("BOOLEAN"),
        Function("FUNCATION"),
        ReternV("RETERN_VALUE");
        fun getType() : kotlin.String{
            return type
        }
    }
    private lateinit var type : ObjectType
    fun getType(): ObjectType {
        return type
    }
    fun inspect(): String {
        return ""
    }
}