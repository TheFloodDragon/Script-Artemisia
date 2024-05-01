package vm.runtime

import compiler.runtime.compiler.core.code.CodePool
import compiler.runtime.compiler.core.module.ModuleCode

class CodeBlock(val index : Int,val type : ModuleCode.BlockType, val size : Int, val name : String, val code : CodePool) {
    override fun toString(): String {
        return "    $index -> {\n" +
                "       size: $size\n"+
                "       type: $type\n"+
                "       name: $name\n"+
                "       code: { \n              ${code.getDatas().toList().joinToString(separator = "\n              ")} \n            } \n        }\n"+
                "   }"
    }
}