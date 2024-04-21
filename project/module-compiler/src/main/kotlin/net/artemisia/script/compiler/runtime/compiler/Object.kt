package net.artemisia.script.compiler.runtime.compiler

import net.artemisia.script.compiler.Compiler

interface Object {
    fun visit(compiler: Compiler) : ArrayList<Byte>
}