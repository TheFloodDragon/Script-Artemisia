package vm.runtime.interpreter

interface ICallable {
    val paramsize : Int
    fun call(params : List<Any?>) : Any?
}