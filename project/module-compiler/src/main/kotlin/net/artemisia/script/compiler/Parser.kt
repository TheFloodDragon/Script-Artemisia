package net.artemisia.script.compiler

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.common.expection.thrower
import net.artemisia.script.common.location.BigLocation
import net.artemisia.script.common.location.Location
import net.artemisia.script.common.token.Token
import net.artemisia.script.common.token.TokenType
import net.artemisia.script.compiler.runtime.parser.initialize.expression.*
import net.artemisia.script.compiler.runtime.parser.initialize.statement.*
import java.io.File

class Parser(val file: File) {
    private val tokens: List<Token> = Lexer(file.readText()).tokens
    private var index = 0
    private var currentToken: Token = tokens[index]
    private var isModuleSet = false
    val isEnd: Boolean = tokens.size <= index || currentToken.type == TokenType.EOF
    var module: State.Module? = null
    private val imports: ArrayList<State.ImportState> = arrayListOf()
    fun parser(): State.Module {
        while (true) {
            if (isEnd || currentToken.type == TokenType.EOF) {
                break
            } else if (match(TokenType.MODULE) && !isModuleSet) {
                module = ModuleStatement().visit(this)
                isModuleSet = true
                break
            } else if (match(TokenType.IMPORT)) {
                imports.add(ImportStatement().visit(this))
            } else if (match(TokenType.NEWLINE)) consume(TokenType.NEWLINE)
            break
        }
        if (module == null) {
            module = ModuleStatement(Expr.Identifier(file.nameWithoutExtension)).visit(this)
        }

        module!!.body.addAll(imports)
        return module!!
    }

    fun getState(): State {
        val start = getLocation()
        val state = when (currentToken.type) {
            TokenType.LEFT_BRACE -> BlockStatement().visit(this)
            TokenType.IDENTIFIER, TokenType.NUMBER, TokenType.STRING, TokenType.BOOLEAN -> {
                val expr = if (check(TokenType.DOT) || check(TokenType.LEFT_SQUARE)) {
                    Member().visit(this)
                } else {
                    getExpr()
                }
                val end = getLocation()
                State.ExpressionState(expr, BigLocation(start, end))
            }
            TokenType.OVERRIDE, TokenType.PRIVATE, TokenType.PUBLIC, TokenType.PROTECTED -> VisitorStatement().visit(
                this
            )
            TokenType.CLASS -> ClassStatement().visit(this)
            TokenType.AT -> AnnotationStatement().visit(this)
            TokenType.STAR,TokenType.ADDRESS -> State.ExpressionState(getExpr(), BigLocation(getLocation(), getLocation()))
            TokenType.FOR -> ForStatement().visit(this)
            TokenType.RETURN -> ReturnStatement().visit(this)
            TokenType.METHOD -> MethodStatement().visit(this)
            TokenType.FINAL -> VariableStatement(true).visit(this)
            TokenType.LET -> VariableStatement().visit(this)



            TokenType.IF -> IfStatement().visit(this)

            else -> {

                if (match(TokenType.DOT)) {
                    return State.ExpressionState(
                        Member(Expr.NullLiteral).visit(this),
                        BigLocation(getLocation(), getLocation())
                    )
                }
                EmptyStatement().visit(this)

            }
        }

        return if (match(TokenType.EQUAL) && state !is State.VariableDeclaration){
            val e = getLocation()
            State.ExpressionState(Assignment(state).visit(this),BigLocation(start,e))
        }else{
            state
        }
    }

    fun expr(): Expr {
        return when (currentToken.type) {
            TokenType.NUMBER, TokenType.STRING -> {
                Literal().visit(this)

            }

            TokenType.BOOLEAN -> {
                Literal().visit(this)

            }

            TokenType.IDENTIFIER -> {
                when (peek().type) {
                    TokenType.LESS -> {
                        var i = index
                        while (i < tokens.size){
                            if (tokens[i].type == TokenType.ARROW){
                                val result = Generic().visit(this)

                                if (match(TokenType.LEFT_PAREN)) {
                                    return Call(result).visit(this)
                                } else {
                                    return result
                                }
                            }else{
                                i += 1
                            }
                            if (i > tokens.size) break
                        }
                        Identifier().visit(this)
                    }

                    TokenType.LEFT_PAREN -> {
                        Call(Identifier().visit(this)).visit(this)

                    }

                    TokenType.STAR -> {
                        val id = Identifier().visit(this)
                        consume(TokenType.STAR)
                        val ret = Expr.PointerExpr(id)
                        if (match(TokenType.STAR)) {
                            thrower.send(
                                "Cant Put more Pointer",
                                "PointerError",
                                file,
                                BigLocation(currentToken.location, currentToken.location),
                                true
                            )

                        }
                        ret
                    }

                    else -> {
                        Identifier().visit(this)
                    }
                }
            }

            else -> {
                Expr.NullLiteral
            }
        }
    }

    fun getExpr(): Expr {
        TokenType.Unary.forEach {
            if (match(it)) {
                return Unary(true).visit(this)
            }
        }

        val init = expr()

        if (match(TokenType.TO)) {
            consume(TokenType.TO)
            return Expr.ToExpr(init, getExpr())
        }
        if (match(TokenType.UNTIL)) {
            consume(TokenType.UNTIL)
            return Expr.UntilExpr(init, getExpr())
        }

        if (match(TokenType.IN)) {
            consume(TokenType.IN)
            return Expr.InExpr(init, getExpr())
        }

        TokenType.logical.forEach {
            if (match(it)) {
                val i = Logical(init).visit(this)
                if (match(TokenType.AND)) {
                    consume(TokenType.AND)
                    return Expr.LogicalExpr(TokenType.AND.id, i, getExpr())
                }
                if (match(TokenType.OR)) {
                    consume(TokenType.OR)
                    return Expr.LogicalExpr(TokenType.OR.id, i, getExpr())
                }
                return i
            }
        }

        TokenType.Binary.forEach {
            if (match(it)) {
                return Binary(init).visit(this)
            }
        }

        TokenType.Unary.forEach {
            if (match(it)) {
                return Unary(false).visit(this)
            }
        }


        return init
    }


    fun advance(): Token {
        if (!isEnd) {
            index += 1
            currentToken = tokens[index]
            return currentToken
        } else {
            return currentToken
        }
    }

    fun spilt() {
        if (currentToken.type != TokenType.NEWLINE && currentToken.type != TokenType.EOF) consume(TokenType.SEMICOLON)
        else if (currentToken.type != TokenType.SEMICOLON && currentToken.type != TokenType.EOF && currentToken.type != TokenType.NEWLINE) consume(
            TokenType.SEMICOLON
        )
        else if (currentToken.type != TokenType.EOF && currentToken.type != TokenType.NEWLINE) consume(TokenType.SEMICOLON)
        else if (currentToken.type == TokenType.NEWLINE) consume(TokenType.NEWLINE)
        else if (currentToken.type == TokenType.SEMICOLON) consume(TokenType.SEMICOLON)

    }

    fun getLocation(): Location {
        return currentToken.location
    }

    fun look(): Token {
        return currentToken
    }

    fun peek(range: Int = 1): Token {
        return tokens[index + range]
    }

    fun check(token: TokenType): Boolean {
        return peek().type == token
    }

    fun check(vararg token: TokenType): Boolean {
        var same = false
        for (i in token) {
            same = peek().type == i
        }
        return same
    }

    fun match(token: TokenType): Boolean {
        return look().type == token
    }

    fun consume(token: TokenType): Token? {
        return if (currentToken.type == token) {
            val i = currentToken
            advance()
            i
        } else {

            thrower.send(
                "Expect Token -> $token but is ${currentToken.type}",
                "Error Token",
                file,
                BigLocation(currentToken.location, currentToken.location)
            )

            null
        }
    }
}