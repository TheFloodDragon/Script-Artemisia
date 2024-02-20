package net.artemisia.script.compiler.runtime.parser

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.compiler.Parser

interface Expression{

    fun visit(parser: Parser) : Expr
}