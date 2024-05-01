package compiler.runtime.compiler

import compiler.Compiler

interface Object {
    fun visit(compiler: Compiler) : ArrayList<Byte>
}