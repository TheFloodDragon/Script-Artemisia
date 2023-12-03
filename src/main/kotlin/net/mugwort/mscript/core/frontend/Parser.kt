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
    private var functions = ArrayList<Statement.FunctionDeclaration>()
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
            TokenType.LET ->{
                val ret = functionStatement()
                functions.add(ret)
                return ret
            }
            TokenType.SEMICOLON -> {
                return Statement.EmptyStatement()
            }
            TokenType.IDENTIFIER -> {
                return if (tokens[currentTokenIndex].type == TokenType.LEFT_PAREN){
                    Statement.ExpressionStatement(callFunction())
                }else {
                    Statement.ExpressionStatement(assignmentExpression())
                }
            }
            else -> {
                return if (currentToken.type != TokenType.EOF) {
                    Statement.ExpressionStatement(expression())
                }else{
                    null
                }
            }
        }
    }

    private fun assignmentExpression(): Expression.AssignmentExpression {
        val left = expression()
        val operator = currentToken.value
        nextToken()
        val right = expression()
        return Expression.AssignmentExpression(left,operator,right)
    }

    private fun callFunction(): Expression.CallExpression {
        var id : Expression = Expression.NullLiteral
        val value : ArrayList<Expression> = arrayListOf()
        for (i in functions){
            if (currentToken.value == i.identifier.name){
                id = expression()
                del(TokenType.LEFT_PAREN)
                while (currentToken.type != TokenType.RIGHT_PAREN){
                    if (currentToken.type == TokenType.COMMA){
                        del(currentToken.type)
                    }
                    value.add(expression())
                }
                del(TokenType.RIGHT_PAREN)
            }else{
                thrower.SyntaxError("Not Found Function ${currentToken.value}")
            }
        }
        return Expression.CallExpression(id,value)
    }
    /**
     * LetStatement
     *
     * | def a(var a : Number,var b : Number): Number { body } |
     *
     * @return Function Statement
     * */
    private fun functionStatement(): Statement.FunctionDeclaration {
        del(TokenType.LET)
        val id = expression() as Expression.Identifier
        val params = ArrayList<Statement.VariableStatement>()
        del(TokenType.LEFT_PAREN)
        while (currentToken.type != TokenType.RIGHT_PAREN){
            if (currentToken.type == TokenType.COMMA){
                del(TokenType.COMMA)
            }
            when(currentToken.type){
                TokenType.CONST ->  params.add(varStatement(true, isParams = true))
                TokenType.VAR -> params.add(varStatement(false,isParams = true))
                else -> {
                    if (currentToken.type == TokenType.IDENTIFIER){
                        params.add(varStatement(false, isParams = true))
                    }
                }
            }
        }
        del(TokenType.RIGHT_PAREN)
        val ret : TokenType = if (currentToken.type == TokenType.COLON){
            del(TokenType.COLON)
            COLONExpression().values.first()
        }else{
            TokenType.VOID
        }
        val body : Statement.BlockStatement = blockStatement()
        return Statement.FunctionDeclaration(id,params,ret,body)
    }
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
     * @return Var
     * */
    private fun varStatement(isConst : Boolean = false,isParams : Boolean = false): Statement.VariableStatement {
        fun declaration(): Statement.VariableDeclaration {
            val id = expression() as Expression.Identifier
            if (currentToken.type == TokenType.COLON){
                if (isConst && !check(TokenType.EQUAL)){
                    thrower.SyntaxError("the Const Var Must be init!")
                }else{
                    del(TokenType.COLON)
                    val init = COLONExpression()

                    if (currentToken.type == TokenType.EQUAL){
                        del(TokenType.EQUAL)
                        val inits = primaryExpression(init.values.first())
                        return Statement.VariableDeclaration(id, inits)
                    }
                    return Statement.VariableDeclaration(id, init.keys.first())
                }
            }

            del(TokenType.EQUAL)
            val init = expression()
            return Statement.VariableDeclaration(id, init)
        }
        if (!isParams){
            if (isConst) {
                del(TokenType.CONST)
                del(TokenType.VAR)
            } else {
                del(TokenType.VAR)
            }
        }
        val declarations = mutableListOf<Statement.VariableDeclaration>()
        declarations.add(declaration())
        if (currentToken.type == TokenType.COMMA && isParams){
            del(TokenType.COMMA)
        }else if (currentToken.type == TokenType.RIGHT_PAREN){
            return Statement.VariableStatement(declarations, isConst)
        } else{
            del(TokenType.SEMICOLON, Translation.InvalidEND.get())
        }
        return Statement.VariableStatement(declarations, isConst)
    }
    private fun expression(): Expression {
        return primaryExpression()
    }
    private fun primaryExpression(locktype : TokenType? = null): Expression {
        return when (currentToken.type) {
            TokenType.IDENTIFIER -> {
                if (locktype != null && locktype != TokenType.IDENTIFIER && locktype != TokenType.OBJECT){
                    thrower.SyntaxError("the variable is locked of $locktype")
                    Expression.NullLiteral
                }else{
                    val identifier = Expression.Identifier(currentToken.value)
                    nextToken()
                    identifier
                }
            }
            TokenType.NUMBER -> {
                if (locktype != null && locktype != TokenType.NUMBER && locktype != TokenType.OBJECT){
                    thrower.SyntaxError("the variable is locked of $locktype")
                    Expression.NullLiteral
                }else{
                    val number = Expression.NumericLiteral(currentToken.value.toDouble())
                    nextToken()
                    number
                }

            }
            TokenType.STRING ->{
                if (locktype != null && locktype != TokenType.STRING && locktype != TokenType.OBJECT){
                    thrower.SyntaxError("the variable is locked of $locktype")
                    Expression.NullLiteral
                }else{
                    val string = Expression.StringLiteral(currentToken.value)
                    nextToken()
                    string
                }
            }
            TokenType.FALSE,TokenType.TRUE -> {
                if (locktype != null && locktype != TokenType.BOOLEAN && locktype != TokenType.OBJECT){
                    thrower.SyntaxError("the variable is locked of $locktype")
                    Expression.NullLiteral
                }else{
                    val boolean = Expression.BooleanLiteral(currentToken.value.toBoolean())
                    nextToken()
                    boolean
                }
            }

            else -> {
                thrower.SyntaxError(Translation.InvalidExpression.get())
                Expression.NullLiteral
            }
        }
    }
    private fun COLONExpression(): MutableMap<Expression,TokenType> {
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
            TokenType.FALSE,TokenType.TRUE -> {
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
            thrower.SyntaxError(error)
        }
        nextToken()
    }
    private fun del(token: TokenType){
        if (!isEnd && currentToken.type != token) {
            println(currentToken)
            thrower.SyntaxError("Not Found -> [\"${token.id}\"]")
        }
        nextToken()
    }
}