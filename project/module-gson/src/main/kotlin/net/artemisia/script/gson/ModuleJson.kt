package gson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import common.ast.Expr
import common.ast.State
import common.location.BigLocation
import kotlin.reflect.full.primaryConstructor


class ModuleJson {
    fun parser(state: State.Module): String? {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val map : MutableMap<Any?,Any?> = mutableMapOf()
        map["name"] = "Module"
        map["id"] = state.id.name
        map["location"] = state.location.toMap()
        val list : ArrayList<MutableMap<Any?,Any?>> = arrayListOf()
        for (i in state.body){
            list.add(getMap(i))
        }
        map["body"] = list
        return gson.toJson(map)
    }

    private fun getMap(state: Any): MutableMap<Any?, Any?> {
        // 获取状态的名称
        val name = state.javaClass.simpleName

        // 如果状态名称为 "EmptyState"，则返回 null
        if (name == "EmptyState") {
            return mutableMapOf()
        }

        // 创建包含状态信息的映射
        val map: MutableMap<Any?, Any?> = mutableMapOf()
        map["name"] = name

        // 获取状态的构造函数和参数
        val constructor = state::class.primaryConstructor
        val parameters = constructor?.parameters
        val parameterValues = parameters?.map { parameter ->
            val property = parameter.name?.let { state.javaClass.getDeclaredField(it) }
            if (property != null) {
                property.isAccessible = true
            }
            property?.get(state)
        }

        // 遍历参数并将其添加到映射中
        parameters?.forEachIndexed { index, parameter ->
            val value = parameterValues?.get(index)
            if (value != null) {
                if (value is BigLocation) {
                    map[parameter.name] = value.toMap()
                } else if (value is State || value is Expr) {
                    map[parameter.name] = getMap(value)
                } else if (value is List<*>) {
                    val list: ArrayList<MutableMap<Any?, Any?>> = arrayListOf()
                    for (i in value) {
                        val subMap = getMap(i!!)
                        if (subMap != null) {
                            list.add(subMap)
                        }
                    }
                    map[parameter.name] = list
                } else {
                    map[parameter.name] = value
                }
            } else {
                map[parameter.name] = getMap(Expr.NullLiteral)
            }
        }
        return map
    }


}