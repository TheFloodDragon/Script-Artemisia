package compiler.runtime.parser

import common.ast.State
import compiler.Parser

interface Statement {
    fun visit(parser: Parser) : State
}