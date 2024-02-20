package net.artemisia.script.gson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.common.location.BigLocation
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.primaryConstructor


class JsonProgram {
    fun parser(state: State.Program): String? {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val map : MutableMap<Any?,Any?> = mutableMapOf()
        map["name"] = "program"
        map["location"] = state.location.toMap()
        val list : ArrayList<MutableMap<Any?,Any?>> = arrayListOf()
        for (i in state.body){
            list.add(getMap(i))
        }
        map["body"] = list
        return gson.toJson(map)
    }

    private fun getMap(state: Any): MutableMap<Any?, Any?> {
        val map: MutableMap<Any?, Any?> = mutableMapOf()
        val name = state.javaClass.simpleName
        map["name"] = name

        val constructor = state::class.primaryConstructor
        val parameters = constructor?.parameters
        val parameterValues = parameters?.map { parameter ->
            val property = parameter.name?.let { state.javaClass.getDeclaredField(it) }
            if (property != null) {
                property.isAccessible = true
            }
            property?.get(state)
        }
        parameters?.forEachIndexed { index, parameter ->
            val value = parameterValues?.get(index)
            if (value != null){
                if (value is BigLocation){
                    map[parameter.name] = value.toMap()
                }else if (value is State || value is Expr){
                    map[parameter.name] = getMap(value)
                }else if (value is List<*>){
                    val list : ArrayList<MutableMap<Any?,Any?>> = arrayListOf()
                    for (i in value){
                        list.add(getMap(i!!))
                    }
                    map[parameter.name] = list
                } else{
                    map[parameter.name] = value
                }
            }else{
                map[parameter.name] = getMap(Expr.NullLiteral)
            }
        }
        return map
    }


}