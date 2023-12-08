package net.mugwort.mscript.core.frontend

import net.mugwort.mscript.ast.core.Expression
import net.mugwort.mscript.ast.core.Statement
import net.mugwort.mscript.ast.token.Token
import net.mugwort.mscript.ast.token.TokenType
import net.mugwort.mscript.runtime.Translation
import net.mugwort.mscript.runtime.expection.thrower
import net.mugwort.mscript.utils.JsonUtils

class Parser(private val tokens: List<Token>) {

    private var currentTokenIndex = 0
    private var currentToken : Token = tokens[currentTokenIndex]
    private var isEnd : Boolean = false
    private val statementList : ArrayList<Statement> = arrayListOf()
    private val expr : Expressions = Expressions()
    fun parser(): Statement.Program {
        while(!isEnd){
            nextToken()
            Statements().statements()?.let { statementList.add(it) }
        }
        return Statement.Program(statementList)
    }
    fun parserJson(): String? {
        return JsonUtils.toJson(parser().toMap())
    }
    private inner class Statements{
        fun statements(): Statement? {
            when (currentToken.type) {
                TokenType.LEFT_BRACE -> return blockStatement()
                TokenType.CONST,TokenType.VAL -> return varStatement(true)
                TokenType.RETURN -> return returnStatement()
                TokenType.VAR -> return varStatement()
                TokenType.LET -> return functionStatement()
                TokenType.SEMICOLON -> return Statement.EmptyStatement()
                TokenType.IDENTIFIER -> {
                    return when (tokens[currentTokenIndex].type) {
                        TokenType.LEFT_PAREN -> {
                            Statement.ExpressionStatement(expr.CallExpression())
                        }
                        TokenType.Incrementing, TokenType.Subtraction -> {
                            Statement.ExpressionStatement(expr.rightUnaryExpression())
                        }
                        else -> {
                            Statement.ExpressionStatement(expr.AssignmentExpression())
                        }
                    }
                }
                TokenType.BANG,TokenType.MINUS ->{
                    return Statement.ExpressionStatement(expr.leftUnaryExpression())
                }
                else -> {
                    return if (currentToken.type != TokenType.EOF) {
                        Statement.ExpressionStatement(expr.expression())
                    }else{
                        null
                    }
                }
            }
        }
        fun varStatement(isConst : Boolean = false,isParams : Boolean = false): Statement.VariableStatement {
            fun declaration(): Statement.VariableDeclaration {
                val id = expr.Identifier()
                if (currentToken.type == TokenType.COLON){
                    if (isConst && !check(TokenType.EQUAL)){
                        thrower.SyntaxError("the Const Var Must be init!")
                    }else{
                        consume(TokenType.COLON)
                        val init = expr.typeExpression()

                        if (currentToken.type == TokenType.EQUAL){
                            consume(TokenType.EQUAL)
                            val inits = expr.primaryExpression()
                            return Statement.VariableDeclaration(id, inits)
                        }
                        return Statement.VariableDeclaration(id, init.keys.first())
                    }
                }
                consume(TokenType.EQUAL)
                val init = expr.expression()
                return Statement.VariableDeclaration(id, init)
            }
            if (!isParams){
                if (isConst) {
                    if (currentToken.type == TokenType.CONST){
                        consume(TokenType.CONST)
                        if (currentToken.type == TokenType.VAL){
                            consume(TokenType.VAL)
                        }
                    }else{
                        consume(TokenType.VAL)
                    }
                } else {
                    consume(TokenType.VAR)
                }
            }else{
                if (isConst) {
                    if (currentToken.type == TokenType.CONST){
                        consume(TokenType.CONST)
                        if (currentToken.type == TokenType.VAL){
                            consume(TokenType.VAL)
                        }
                    }

                } else {
                    if (currentToken.type == TokenType.VAR) {
                        consume(TokenType.VAR)
                    }
                }
            }
            val declarations = mutableListOf<Statement.VariableDeclaration>()
            declarations.add(declaration())
            if (currentToken.type == TokenType.COMMA && isParams){
                consume(TokenType.COMMA)
            }else if (currentToken.type == TokenType.RIGHT_PAREN){
                return Statement.VariableStatement(declarations, isConst)
            } else{
                consume(TokenType.SEMICOLON, Translation.InvalidEND.get())
            }
            return Statement.VariableStatement(declarations, isConst)
        }
        fun blockStatement(): Statement.BlockStatement {
            consume(TokenType.LEFT_BRACE)
            val statements = ArrayList<Statement>()
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                statements()?.let { statements.add(it) }
            }
            return Statement.BlockStatement(statements)
        }
        fun functionStatement(): Statement.FunctionDeclaration {
            consume(TokenType.LET)
            val id = expr.Identifier()
            val params = ArrayList<Statement.VariableStatement>()
            consume(TokenType.LEFT_PAREN)
            while (currentToken.type != TokenType.RIGHT_PAREN){
                if (currentToken.type == TokenType.COMMA){
                    consume(TokenType.COMMA)
                }
                when(currentToken.type){
                    TokenType.CONST,TokenType.VAL ->  params.add(varStatement(true, isParams = true))
                    TokenType.VAR -> params.add(varStatement(false,isParams = true))
                    else -> {
                        if (currentToken.type == TokenType.IDENTIFIER){
                            params.add(varStatement(false, isParams = true))
                        }
                    }
                }
            }
            consume(TokenType.RIGHT_PAREN)
            val ret : TokenType = if (currentToken.type == TokenType.COLON){
                consume(TokenType.COLON)
                expr.typeExpression().values.first()
            }else{
                TokenType.VOID
            }
            val body : Statement.BlockStatement = blockStatement()
            return Statement.FunctionDeclaration(id,params,ret,body)
        }
        fun returnStatement(): Statement.ReturnStatement {
            consume(TokenType.RETURN)
            val argument : Expression =
                when(currentToken.type){
                    TokenType.IDENTIFIER -> {
                        if (tokens[currentTokenIndex].type == TokenType.LEFT_PAREN) {
                            expr.CallExpression()
                        }else{
                            expr.Identifier()
                        }
                    }
                    else -> {
                        expr.primaryExpression()
                    }
                }
            if (argument !is Expression.CallExpression) consume(TokenType.SEMICOLON)
            return Statement.ReturnStatement(argument)
        }
    }
    private inner class Expressions{
        val complex = listOf(
            TokenType.PLUS_EQUAL,
            TokenType.MINUS_EQUAL,
            TokenType.STAR_EQUAL,
            TokenType.SLASH_EQUAL,
            TokenType.MODULUS_EQUAL
        )
        val binary = listOf(
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.STAR,
            TokenType.MODULUS,
            TokenType.SLASH
        )
        val logical = listOf(
        TokenType.OR,
        TokenType.AND,
        TokenType.LESS_EQUAL,
        TokenType.BANG_EQUAL,
        TokenType.GREATER,
        TokenType.GREATER_EQUAL,
        TokenType.LESS,
        TokenType.EQUAL_EQUAL
        )

        fun isBinaryOperator(): Boolean {
            return binary.contains(currentToken.type)
        }
        fun isLogicalOperator(): Boolean {
            return logical.contains(currentToken.type)
        }
        fun isAssignmentOperator(): Token {
            if (currentToken.type == TokenType.EQUAL) return consume(TokenType.EQUAL)
            return when(currentToken.type){
                TokenType.PLUS_EQUAL ->  consume(complex[0])
                TokenType.MINUS_EQUAL -> consume(complex[1])
                TokenType.STAR_EQUAL -> consume(complex[2])
                TokenType.SLASH_EQUAL -> consume(complex[3])
                TokenType.MODULUS_EQUAL -> consume(complex[4])
                else -> consume(TokenType.EQUAL)
            }
        }
        fun AssignmentExpression(): Expression.AssignmentExpression {
            val left = expression()
            val operator = isAssignmentOperator().value
            nextToken()
            val right = expression()
            consume(TokenType.SEMICOLON)
            return Expression.AssignmentExpression(left,operator,right)
        }
        fun CallExpression(): Expression.CallExpression {
            val value : ArrayList<Expression> = arrayListOf()
            val id = Identifier()
            consume(TokenType.LEFT_PAREN)
            while (currentToken.type != TokenType.RIGHT_PAREN){
                if (currentToken.type == TokenType.COMMA){
                    consume(currentToken.type)
                }
                value.add(expression())
            }
            consume(TokenType.RIGHT_PAREN)
            consume(TokenType.SEMICOLON)
            return Expression.CallExpression(id,value)
        }
        fun expression(): Expression {
            if (currentToken.type == TokenType.MINUS || currentToken.type == TokenType.BANG) {
                val operator = currentToken.value
                nextToken()
                val right = primaryExpression()
                return Expression.UnaryExpression(operator, right)
            }else if (tokens[currentTokenIndex].type == TokenType.Incrementing || tokens[currentTokenIndex].type == TokenType.Subtraction){
                val operator = tokens[currentTokenIndex].value
                val left = primaryExpression()
                nextToken()
                return Expression.UnaryExpression(operator, left)
            } else {
                var left = primaryExpression()
                while (isBinaryOperator()) {
                    val operator = currentToken.value
                    nextToken()
                    val right = primaryExpression()
                    left = Expression.BinaryExpression(operator, left, right)
                }
                while (isLogicalOperator()) {
                    val operator = currentToken.value
                    nextToken()
                    val right = primaryExpression()
                    left = Expression.LogicalExpression(operator, left, right)
                }
                return left
            }

        }
        fun typeExpression(): MutableMap<Expression,TokenType> {
            return when (currentToken.type) {
                TokenType.IDENTIFIER -> {
                    val identifier = Expression.Identifier(currentToken.value)
                    nextToken()
                    mutableMapOf(identifier to TokenType.IDENTIFIER)
                }
                TokenType.NUMBER -> {
                    val number = Expression.NumericLiteral(null)
                    nextToken()
                    mutableMapOf(number to TokenType.NUMBER)
                }
                TokenType.STRING ->{
                    val string = Expression.StringLiteral(null)
                    nextToken()
                    mutableMapOf(string to TokenType.STRING)
                }
                TokenType.BOOLEAN -> {
                    val boolean = Expression.BooleanLiteral(null)
                    nextToken()
                    mutableMapOf(boolean to TokenType.BOOLEAN)
                }
                TokenType.OBJECT -> {
                    val obj = Expression.ObjectLiteral(null)
                    nextToken()
                    mutableMapOf(obj to TokenType.OBJECT)
                }
                else -> {
                    thrower.SyntaxError(Translation.InvalidExpression.get())
                    mutableMapOf(Expression.NullLiteral to TokenType.NULL)
                }
            }
        }
        fun leftUnaryExpression() : Expression.UnaryExpression{
            val operator = currentToken.value
            nextToken()
            val right = primaryExpression()
            consume(TokenType.SEMICOLON)
            return Expression.UnaryExpression(operator, right)
        }
        fun rightUnaryExpression() : Expression.UnaryExpression{
            val operator = tokens[currentTokenIndex].value
            val left = primaryExpression()
            nextToken()
            consume(TokenType.SEMICOLON)
            return Expression.UnaryExpression(operator, left)
        }
        fun Identifier(): Expression.Identifier {
            return Expression.Identifier(consume(TokenType.IDENTIFIER).value)
        }
        fun primaryExpression(): Expression {
            return when(currentToken.type){
                TokenType.IDENTIFIER -> Identifier()
                TokenType.LEFT_PAREN -> {
                    consume(TokenType.LEFT_PAREN)
                    val ret = Expression.GroupExpression(expression())
                    consume(TokenType.RIGHT_PAREN)
                    ret
                }
                else -> Literal().literal
            }
        }

    }
    private inner class Literal{
        val literal: Expression = when(currentToken.type){
            TokenType.NUMBER ->  NumericLiteral()
            TokenType.STRING ->  StringLiteral()
            TokenType.FALSE,TokenType.TRUE ->  BooleanLiteral()
            TokenType.NULL -> NullLiteral()
            TokenType.OBJECT -> ObjectLiteral()
            else -> {
                println(currentToken)
                thrower.SyntaxError("Literal: unexpected literal production")
                Expression.NullLiteral
            }
        }
        val isLiteral : Boolean =
            currentToken.type == TokenType.NUMBER ||
            currentToken.type == TokenType.STRING ||
            currentToken.type == TokenType.FALSE ||
            currentToken.type == TokenType.TRUE ||
            currentToken.type == TokenType.OBJECT


        fun BooleanLiteral(): Expression.BooleanLiteral {
            return Expression.BooleanLiteral(consume(if (currentToken.type == TokenType.TRUE) TokenType.TRUE else TokenType.FALSE).value.toBoolean())
        }
        fun NumericLiteral(): Expression.NumericLiteral {
            return Expression.NumericLiteral(consume(TokenType.NUMBER).value.toDouble())
        }
        fun StringLiteral() : Expression.StringLiteral {
            return Expression.StringLiteral(consume(TokenType.STRING).value)
        }
        fun ObjectLiteral() : Expression.ObjectLiteral {
            return Expression.ObjectLiteral(consume(currentToken.type).value)
        }
        fun NullLiteral(): Expression.NullLiteral {
            consume(TokenType.NULL)
            return Expression.NullLiteral
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
    private fun consume(token: TokenType, error:String): Token {
        val ctoken = currentToken
        if (currentToken.type != token){
            thrower.SyntaxError(error)
        }
        nextToken()
        return ctoken
    }
    private fun consume(token: TokenType): Token {
        val ctoken = currentToken
        if (!isEnd && ctoken.type != token) {
            thrower.SyntaxError("Expect Token -> [\"${token.id}\"] but not Found? Just Found [\"${currentToken.value}\"]")
        }
        nextToken()
        return ctoken
    }
}