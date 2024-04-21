package net.artemisia.script.compiler.runtime.parser

import net.artemisia.script.common.ast.State
import net.artemisia.script.compiler.Parser

interface Statement {
    fun visit(parser: Parser) : State
}