package net.mscript.runtime

import java.util.*

enum class Translation(private val EN_US: String, private val ZH_CN : String) {
    RuntimeException("RuntimeException","运行错误"),
    ExceptionOf("Exception of ","发生了一个"),
    SyntaxError("SyntaxError","语法错误"),
    InvalidEND("The token [';'] must not end of expression!","标识符 [';'] 没有在表达式的结尾！"),
    InvalidExpression("Invalid expression","未知的表达式");
    fun get(): String {
        val defaultLocale = Locale.getDefault()
        val language = defaultLocale.language
        return when (language) {
            "en" -> EN_US
            "zh" -> ZH_CN
            else -> EN_US
        }
    }
}