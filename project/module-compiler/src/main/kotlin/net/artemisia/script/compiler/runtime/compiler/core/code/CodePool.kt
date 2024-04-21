package net.artemisia.script.compiler.runtime.compiler.core.code


class CodePool {
    private val datas = arrayListOf<CodeItem>()
    fun getDatas(): ArrayList<CodeItem> = datas

    fun push(type: CodeType, value : Int? = null): CodeItem {
        val i = CodeItem(type,value)
        datas.add(i)
        return i
    }
    fun indexOf(item : CodeItem): Int {
        return datas.indexOf(item)
    }
}