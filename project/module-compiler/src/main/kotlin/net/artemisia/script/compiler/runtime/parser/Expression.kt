package compiler.runtime.parser

import common.ast.Expr
import compiler.Parser

interface Expression{
    fun visit(parser: Parser) : Expr
}