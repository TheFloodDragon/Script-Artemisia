package net.mugwort.mscript.compiler

import net.mugwort.mscript.core.ast.token.Token
import net.mugwort.mscript.core.ast.token.TokenType
import net.mugwort.mscript.runtime.expection.thrower


class Lexer(private var source: String) {
    var tokens : ArrayList<Token> = ArrayList()
    private var start = 0
    private var current = 0
    private var line = 1
    private var column = 1
    private val keyWords: MutableMap<String, TokenType> = mutableMapOf(
        "case" to TokenType.CASE,
        "switch" to TokenType.SWITCH,
        "public" to TokenType.PUBLIC,
        "private" to TokenType.PRIVATE,
        "try" to TokenType.TRY,
        "catch" to TokenType.CATCH,
        "val" to TokenType.VAL,
        "this" to TokenType.THIS,
        "super" to TokenType.SUPER,
        "do" to TokenType.DO,
        "let" to TokenType.LET,
        "in" to TokenType.IN,
        "return" to TokenType.RETURN,
        "Number" to TokenType.NUMBER,
        "String" to TokenType.STRING,
        "Void" to TokenType.VOID,
        "Boolean" to TokenType.BOOLEAN,
        "Object" to TokenType.OBJECT,
        "class" to TokenType.CLASS,
        "or" to TokenType.OR,
        "break" to TokenType.BREAK,
        "continue" to TokenType.CONTINUE,
        "else" to TokenType.ELSE,
        "false" to TokenType.FALSE,
        "for" to TokenType.FOR,
        "if" to TokenType.IF,
        "const" to TokenType.CONST,
        "import" to TokenType.IMPORT,
        "null" to TokenType.NULL,
        "while" to TokenType.WHILE,
        "true" to TokenType.TRUE,
        "var" to TokenType.VAR,
    )

    init {
        synchronized(this){
            current = start
            scanToken()
            tokens.add(Token(TokenType.EOF, ""))

        }
    }
    private fun scanToken(){
        while (!isEnd()){
            start = current
            when(val c = advance()){
                '(', ')', '.', ',','{',']','[','}',':',';' -> addToken(typeFinder())
                '-' -> addToken(if (match('=')) typeFinder() else if(match('-')) typeFinder() else typeFinder())
                '+' -> addToken(if (match('=')) typeFinder() else if(match('+')) typeFinder() else typeFinder())
                '*' -> addToken(if (match('=')) typeFinder() else typeFinder())
                '/' -> {
                    if (match('=')){
                        addToken(typeFinder())
                    } else addToken(typeFinder())
                }
                '#' -> {
                    if (match('*')) {
                        while (!isEnd()) {
                            if (look() == '*' && lookNext() == '#') {
                                advance()
                                advance()
                                break
                            }
                            if (look() == '\n') {
                                line++
                                column = 0
                            }
                            advance()
                        }
                    }else{
                        while (look() != '\n' && !isEnd()) {
                            advance()
                        }
                    }
                }
                '%' -> addToken(if (match('=')) typeFinder() else typeFinder())
                '!' -> addToken(if (match('=')) typeFinder() else typeFinder())
                '>' -> addToken(if (match('=')) typeFinder() else typeFinder())
                '<' -> addToken(if (match('=')) typeFinder() else typeFinder())
                '=' -> {
                    if (match('>')) {
                        addToken(typeFinder())
                    } else if (match('=')) {
                        addToken(typeFinder())
                    } else {
                        addToken(typeFinder())
                    }
                }
                '&' -> {
                    if (match('&')) {
                        addToken(typeFinder())
                    } else {
                        fatal("Unexpected character '&'")
                    }
                }
                '|' -> {
                    if (match('|')) {
                        addToken(typeFinder())
                    } else {
                        fatal("Unexpected character '|'")
                    }
                }
                '\n' -> {

                }
                ' ', '\t', '\r' -> Unit
                '"', '\'', '`' -> setString(c)
                else -> {
                    if (isNumber(c)) {
                        setNumber()
                    } else if (isString(c)) {
                        setIdentifier()
                    } else {
                        fatal("Unexpected character '$c'")

                    }
                }
            }
            if (isEnd()) break
        }


    }
    private fun setString(quote: Char) {
        while ((look() != quote) && !isEnd()) {
            if (look() == '\n') {
                line++
                column = 0
            }
            if (look() == '\\') advance()
            advance()
        }
        if (isEnd()) {
            fatal("Unterminated string")
            return
        }
        advance()
        addToken(TokenType.STRING, source.substring(start + 1, current - 1))
    }
    private fun setNumber(){
        while (isNumber(look())){
            advance()
        }
        if (look() == '.' && isNumber(lookNext())){
            advance()
            while (isNumber(look())){
                advance()
            }
        }
        addToken(TokenType.NUMBER, source.substring(start, current))
    }
    private fun setIdentifier(){
        while (isNumber(look()) || isString(look())){
            advance()
        }
        val str = source.substring(start, current)
        val key = keyWords[str]
        if (key != null){
            addToken(key)
        }else{
            addToken(TokenType.IDENTIFIER,source.substring(start, current))
        }
    }
    private fun isNumber(char: Char): Boolean {
        return char in '0'..'9'
    }
    private fun isString(char: Char): Boolean{
        return char in 'A'..'Z' || char in 'a'..'z' || char == '_';
    }
    private fun look(): Char {
        return if (isEnd()) '\u0000' else source[current]
    }
    private fun lookNext():Char{
        return if (current + 1 >= source.length) '\u0000' else source[current + 1]
    }
    private fun match(c: Char): Boolean {
        if (look() != c) return false
        advance()
        return true
    }


    private fun advance(): Char {
        column++
        return source[current++]
    }
    private fun isEnd(): Boolean {
        return current >= source.length
    }
    private fun typeFinder(index : Int = 0) : TokenType {
        val id: String = source.substring(start, current + index)
        return TokenType.fromId(id.lowercase())!!
    }
    private fun addToken(type: TokenType) {
        addToken(type,typeFinder().id)
    }
    private fun addToken(type: TokenType, literal:String){
        tokens.add(Token(type,literal))
    }
    private fun fatal(text:String){
        thrower.RuntimeException(text)
    }
    fun printf(){
        for (i in tokens){
            println(i)
        }
    }
}
