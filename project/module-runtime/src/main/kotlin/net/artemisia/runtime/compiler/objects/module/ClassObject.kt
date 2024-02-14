package net.artemisia.runtime.compiler.objects.module

import net.artemisia.runtime.compiler.objects.other.IdentifierObject

class ClassObject(
    private val id: IdentifierObject,
    private val code: ArrayList<CodeObject>,
    private val functions: ArrayList<FunctionObject>,
    private val visitors: ArrayList<VisitorObject>
) {
}