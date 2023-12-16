package net.mugwort.mscript.runtime

interface ICallable {
    /**
     * ParamCount:
     *  表示可调用对象的参数个数
     * */
    val paramCount: Int

    /**
     * Call: 表示调用可调用对象
     *  @param arguments 类型为List<object?>，表示传递给可调用对象的参数列表~
     *
     * */
    fun call(arguments: List<Any?>): Any?

}