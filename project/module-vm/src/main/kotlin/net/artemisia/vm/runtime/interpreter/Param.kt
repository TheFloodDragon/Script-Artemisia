package vm.runtime.interpreter

import vm.runtime.CodeBlock

data class Param(val name : String,val type : String,val init : CodeBlock? = null,val cnt : Boolean)