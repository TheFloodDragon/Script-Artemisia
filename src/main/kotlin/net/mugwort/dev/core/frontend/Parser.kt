package net.mugwort.dev.core.frontend

import net.mugwort.dev.ast.core.Expression
import net.mugwort.dev.ast.core.Statement
import net.mugwort.dev.ast.token.Token
import net.mugwort.dev.ast.token.TokenType
import net.mugwort.dev.runtime.Translation
import net.mugwort.dev.runtime.expection.thrower
import net.mugwort.dev.utils.JsonUtils

class Parser(private val tokens: List<Token>) {
    private var currentTokenIndex = 0
    private var currentToken : Token = tokens[currentTokenIndex]
    private var isEnd : Boolean = false
    private val statementList : ArrayList<Statement> = arrayListOf()

    fun parser(): Statement.Program {

        while(!isEnd){
            nextToken()
            statements()
        }
        return Statement.Program(statementList)
    }

    fun parserJson(): String? {
        return JsonUtils.toJson(parser().toMap())
    }


    private fun statements() {
        when (currentToken.type) {
            TokenType.VAR -> {
                statementList.add(varStatement())
                if (!isEnd) {
                    statements()
                }
            }
            else -> {
                Statement.EmptyStatement()
            }
        }
    }

    private fun varStatement(): Statement.VariableStatement {
        fun declaration(): Statement.VariableDeclaration {
            val id = expression() as Expression.Identifier
            del(TokenType.EQUAL)
            val init = expression()
            return Statement.VariableDeclaration(id, init)
        }
        del(TokenType.VAR)
        val declarations = mutableListOf<Statement.VariableDeclaration>()
        declarations.add(declaration())
        del(TokenType.SEMICOLON, Translation.InvalidEND.get())
        return Statement.VariableStatement(declarations)
    }

    private fun expression(): Expression {
        return primaryExpression()
    }

    private fun primaryExpression(): Expression {
        return when (currentToken.type) {
            TokenType.IDENTIFIER -> {
                val identifier = Expression.Identifier(currentToken.value)
                nextToken()
                identifier
            }
            TokenType.NUMBER -> {
                val number = Expression.NumericLiteral(currentToken.value.toDouble())
                nextToken()
                number
            }
            TokenType.STRING ->{
                val string = Expression.StringLiteral(currentToken.value)
                nextToken()
                string
            }
            TokenType.FALSE,TokenType.TRUE -> {
                val boolean = Expression.BooleanLiteral(currentToken.value.toBoolean())
                nextToken()
                boolean
            }

            else -> {
                thrower.SyntaxError(Translation.InvalidExpression.get())
                Expression.NullLiteral
            }
        }
    }




    private fun peek() : Token?{
        val nextIndex = currentTokenIndex + 1
        return if (nextIndex < tokens.size) tokens[nextIndex] else null
    }

    private fun check(token: TokenType): Boolean {
        return peek()?.type == token
    }

    private fun nextToken(): Token {
        currentToken = if (currentTokenIndex < tokens.size || currentToken.type != TokenType.EOF) tokens[currentTokenIndex ++] else {
            isEnd = true
            if (currentToken.type == TokenType.EOF) isEnd = true
            currentToken
        }
        return currentToken
    }

    fun del(token: TokenType,error:String){
        if (currentToken.type != token){
            thrower.SyntaxError(error)
        }
        nextToken()
    }

    fun del(token: TokenType){
        if (currentToken.type != token){
            thrower.SyntaxError("Not Found -> [\"${token.id}\"]")
        }
        nextToken()
    }
}