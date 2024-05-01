package vm.runtime.interpreter.method

import common.expection.thrower
import vm.Interpreter
import vm.runtime.CodeBlock
import vm.runtime.interpreter.ICallable
import vm.runtime.interpreter.Param
import vm.runtime.interpreter.core.Environment

class Method(private val args : List<Param>,val interpreter: Interpreter,val block : CodeBlock?,val env : Environment) : ICallable {
    override val paramsize: Int
        get() = args.size

    override fun call(params: List<Any?>): Any? {
        if (params.size < args.size) thrower.SyntaxError("The size is less than param size")
        for (i in args){
            if (i.init != null && params[args.indexOf(i)] == null){
                interpreter.run(i.init)
                env.define(i.name,interpreter.stack.top())
            }else env.define(i.name,params[args.indexOf(i)])
        }
        if (block != null) interpreter.run(block)
        return interpreter.ret
    }

}