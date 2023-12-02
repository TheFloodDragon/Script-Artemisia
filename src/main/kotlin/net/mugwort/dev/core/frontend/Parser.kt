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
            statements()?.let { statementList.add(it) }
        }
        return Statement.Program(statementList)
    }

    fun parserJson(): String? {
        return JsonUtils.toJson(parser().toMap())
    }

    /**
     * Statement Parser:
     *   ExpressionStatement{
     *   -> BlockStatement
     *   -> EmptyStatement
     *   -> VariableStatement
     *   -> IfStatement
     *   -> IterationStatement
     *   -> FunctionDeclaration
     *   -> ClassDeclaration
     *   -> ReturnStatement
     *   }
     */



    private fun statements(): Statement? {
        when (currentToken.type) {
            /**
             * Parser Block Statement
             * @see blockStatement
             * */
            TokenType.LEFT_BRACE -> {
                return blockStatement()
            }
            /**
             * Parser Const Var Statement
             * @see varStatement
             * */
            TokenType.CONST -> {
                return varStatement(true)
            }
            /**
             * Parser Var Statement
             * @see varStatement
             * */
            TokenType.VAR -> {
                return varStatement()
            }

            TokenType.SEMICOLON -> {
                return Statement.EmptyStatement()
            }
            else -> {
                if (currentToken.type != TokenType.EOF) {
                    return Statement.ExpressionStatement(expression())
                }else{
                    return null
                }
            }
        }
    }

//    /**
//     * DefStatement
//     *
//     * | def a(var a : Number, var b : Number): Number { body } |
//     *
//     * @return Block Statement
//     * */
//    private fun defStatement(): Statement.FunctionDeclaration {
//        del(TokenType.DEF)
//        val id = expression() as Expression.Identifier
//
//        val params = ArrayList<Expression.Identifier>()
//        return Statement.FunctionDeclaration(id,params)
//    }

    /**
     * BlockStatement
     *
     * | { body } |
     *
     * @return Block Statement
     * */
    private fun blockStatement(): Statement.BlockStatement {

        del(TokenType.LEFT_BRACE)
        val statements = ArrayList<Statement>()
        while (currentToken.type != TokenType.RIGHT_BRACE) {
            statements()?.let { statements.add(it) }
        }
        return Statement.BlockStatement(statements)
    }
    /**
     * VarStatement
     *
     * | var {Identifier} = {Number | String | Boolean |... }; |
     * | const var {Identifier} = {Number | String | Boolean |... }; |
     *
     * @return Var Statement
     * */
    private fun varStatement(isConst : Boolean = false): Statement.VariableStatement {
        fun declaration(): Statement.VariableDeclaration {
            val id = expression() as Expression.Identifier
            if (currentToken.type == TokenType.COLON){
                if (isConst && !check(TokenType.EQUAL)){
                    thrower.SyntaxError("the Const Var Must be init!")
                }else{
                    del(TokenType.COLON)
                    val init = COLONExpression()
                    if (check(TokenType.EQUAL)){
                        del(TokenType.EQUAL)
                        val inits = expression()
                        return Statement.VariableDeclaration(id, inits)
                    }
                    return Statement.VariableDeclaration(id, init)
                }
            }
            del(TokenType.EQUAL)
            val init = expression()
            return Statement.VariableDeclaration(id, init)
        }
        if (isConst) {
            del(TokenType.CONST)
            del(TokenType.VAR)
        } else {
            del(TokenType.VAR)
        }
        val declarations = mutableListOf<Statement.VariableDeclaration>()
        declarations.add(declaration())
        del(TokenType.SEMICOLON, Translation.InvalidEND.get())
        return Statement.VariableStatement(declarations, isConst)
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
    private fun COLONExpression(): Expression {
        return when (currentToken.type) {
            TokenType.IDENTIFIER -> {
                val identifier = Expression.Identifier(currentToken.value)
                nextToken()
                identifier
            }
            TokenType.NUMBER -> {
                val number = Expression.NumericLiteral(null)
                nextToken()
                number
            }
            TokenType.STRING ->{
                val string = Expression.StringLiteral(null)
                nextToken()
                string
            }
            TokenType.FALSE,TokenType.TRUE -> {
                val boolean = Expression.BooleanLiteral(null)
                nextToken()
                boolean
            }

            else -> {
                thrower.SyntaxError(Translation.InvalidExpression.get())
                Expression.NullLiteral
            }
        }
    }

    private fun back(){
        if (!isEnd) {
            statements()
        }
    }
    private fun peek() : Token?{
        val nextIndex = currentTokenIndex + 1
        return if (nextIndex < tokens.size) tokens[nextIndex] else null
    }
    private fun lastToken(index:Int = 2) : Token{
        return tokens[currentTokenIndex - index]
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

    private fun del(token: TokenType, error:String){
        if (currentToken.type != token){
            println(currentToken.type)
            thrower.SyntaxError(error)
        }
        nextToken()
    }

    private fun del(token: TokenType){
        if (!isEnd && currentToken.type != token) {
            thrower.SyntaxError("Not Found -> [\"${token.id}\"]")
        }

        nextToken()
    }
}