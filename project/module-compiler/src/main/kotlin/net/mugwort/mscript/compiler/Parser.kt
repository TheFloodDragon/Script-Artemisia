package net.mugwort.mscript.compiler

import net.mugwort.mscript.core.ast.core.Expression
import net.mugwort.mscript.core.ast.core.Statement
import net.mugwort.mscript.core.ast.token.Token
import net.mugwort.mscript.core.ast.token.TokenType
import net.mugwort.mscript.runtime.expection.thrower
import net.mugwort.mscript.runtime.other.Translation
import net.mugwort.mscript.utils.JsonUtils

class Parser(private val tokens: List<Token>) {

    private var currentTokenIndex = 0
    private var currentToken: Token = tokens[currentTokenIndex]
    private var isEnd: Boolean = false
    private val statementList: ArrayList<Statement> = arrayListOf()
    private val expr: Expressions = Expressions()
    fun parser(): Statement.Program {
        for (i in tokens){
            nextToken()
            Statements().statements()?.let { statementList.add(it) }
        }
        return Statement.Program(statementList)
    }

    fun parserJson(): String? {
        return JsonUtils.toJson(parser().toMap())
    }

    private inner class Statements {
        fun statements(): Statement? {
            when (currentToken.type) {
                TokenType.LEFT_BRACE -> return blockStatement()
                TokenType.CONST, TokenType.VAL -> return varStatement(true)
                TokenType.RETURN -> return returnStatement()
                TokenType.VAR -> return varStatement()
                TokenType.TRY -> return tryStatement()
                TokenType.SWITCH -> return switchStatement()
                TokenType.LET -> return functionStatement()
                TokenType.IMPORT -> return importStatement()
                TokenType.SEMICOLON -> return Statement.EmptyStatement()
                TokenType.PRIVATE, TokenType.PUBLIC -> return visitorStatement()
                TokenType.DO -> return doWhileStatement()
                TokenType.FOR -> return forStatement()
                TokenType.WHILE -> return whileStatement()
                TokenType.CLASS -> return classStatement()
                TokenType.IDENTIFIER -> {
                    println(tokens[currentTokenIndex].type)
                    return when (tokens[currentTokenIndex].type) {
                        TokenType.LEFT_PAREN -> {
                            val ret = Statement.ExpressionStatement(expr.CallExpression())
                            consume(TokenType.SEMICOLON)
                            ret
                        }

                        TokenType.DOT, TokenType.LEFT_SQUARE -> expr.MemberExpression()
                            ?.let { Statement.ExpressionStatement(it) }

                        TokenType.Incrementing, TokenType.Subtraction -> {
                            Statement.ExpressionStatement(expr.rightUnaryExpression())
                        }

                        else -> {
                            Statement.ExpressionStatement(expr.AssignmentExpression())
                        }
                    }
                }

                TokenType.BANG, TokenType.MINUS,TokenType.Incrementing,TokenType.Subtraction -> {
                    return Statement.ExpressionStatement(expr.leftUnaryExpression())
                }

                TokenType.IF -> return ifStatement()
                else -> {
                    return if (currentToken.type != TokenType.EOF) {
                        Statement.ExpressionStatement(expr.expression())
                    } else {
                        null
                    }
                }
            }
        }

        fun paramGetter(): ArrayList<Statement.VariableStatement> {
            val params = ArrayList<Statement.VariableStatement>()
            consume(TokenType.LEFT_PAREN)
            while (currentToken.type != TokenType.RIGHT_PAREN) {
                if (currentToken.type == TokenType.COMMA) {
                    consume(TokenType.COMMA)
                }
                when (currentToken.type) {
                    TokenType.CONST, TokenType.VAL -> params.add(varStatement(true, isParams = true))
                    TokenType.VAR -> params.add(varStatement(false, isParams = true))
                    else -> {
                        if (currentToken.type == TokenType.IDENTIFIER) {
                            params.add(varStatement(false, isParams = true))
                        }
                    }
                }
            }
            consume(TokenType.RIGHT_PAREN)
            return params
        }

        fun varStatement(isConst: Boolean = false, isParams: Boolean = false): Statement.VariableStatement {
            fun declaration(): Statement.VariableDeclaration {
                val id = expr.Identifier()
                if (currentToken.type == TokenType.COLON) {
                    if (isConst && !check(TokenType.EQUAL)) {
                        thrower.SyntaxError("the Const Var Must be init!")
                    } else {
                        consume(TokenType.COLON)
                        val init = expr.typeExpression()

                        if (currentToken.type == TokenType.EQUAL) {
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
            if (!isParams) {
                if (isConst) {
                    if (currentToken.type == TokenType.CONST) {
                        consume(TokenType.CONST)
                        if (currentToken.type == TokenType.VAL) {
                            consume(TokenType.VAL)
                        }
                    } else {
                        consume(TokenType.VAL)
                    }
                } else {
                    consume(TokenType.VAR)
                }
            } else {
                if (isConst) {
                    if (currentToken.type == TokenType.CONST) {
                        consume(TokenType.CONST)
                        if (currentToken.type == TokenType.VAL) {
                            consume(TokenType.VAL)
                        }
                    }

                } else {
                    if (currentToken.type == TokenType.VAR) {
                        consume(TokenType.VAR)
                    }
                }
            }
            val declaration = declaration()

            if (currentToken.type == TokenType.COMMA && isParams) {
                consume(TokenType.COMMA)
            } else if (currentToken.type == TokenType.RIGHT_PAREN) {
                return Statement.VariableStatement(declaration, isConst)
            } else {
                consume(TokenType.SEMICOLON, Translation.InvalidEND.get())
            }
            return Statement.VariableStatement(declaration, isConst)
        }

        fun blockStatement(): Statement.BlockStatement {
            consume(TokenType.LEFT_BRACE)
            val statements = ArrayList<Statement>()
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                statements()?.let { statements.add(it) }
            }
            consume(TokenType.RIGHT_BRACE)
            return Statement.BlockStatement(statements)
        }

        fun functionStatement(): Statement.FunctionDeclaration {
            consume(TokenType.LET)
            val id = expr.Identifier()
            val params = paramGetter()

            val body: Statement.BlockStatement = blockStatement()
            return Statement.FunctionDeclaration(id, params, body)
        }

        fun returnStatement(): Statement.ReturnStatement {
            consume(TokenType.RETURN)
            val argument: Expression =
                when (currentToken.type) {
                    TokenType.IDENTIFIER -> {
                        if (tokens[currentTokenIndex].type == TokenType.LEFT_PAREN) {
                            expr.CallExpression()
                        } else {
                            expr.Identifier()
                        }
                    }

                    else -> {
                        expr.expression()
                    }
                }
            if (argument !is Expression.CallExpression) consume(TokenType.SEMICOLON)
            return Statement.ReturnStatement(argument)
        }

        fun whileStatement(): Statement.WhileStatement {
            consume(TokenType.WHILE)
            consume(TokenType.LEFT_PAREN)
            val rule = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            val body = blockStatement()
            return Statement.WhileStatement(rule, body)
        }

        fun doWhileStatement(): Statement.DoWhileStatement {
            consume(TokenType.DO)
            val body = blockStatement()
            consume(TokenType.WHILE)
            consume(TokenType.LEFT_PAREN)
            val rule = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            return Statement.DoWhileStatement(body, rule)
        }

        fun classStatement(): Statement.ClassDeclaration {
            consume(TokenType.CLASS)
            val id = expr.Identifier()
            if (currentToken.type == TokenType.LEFT_PAREN) {
                val params = paramGetter()
                val body = blockStatement()
                return Statement.ClassDeclaration(id, params, body)
            }
            val body = blockStatement()

            return Statement.ClassDeclaration(id, null, body)
        }

        fun ifStatement(): Statement.IfStatement {
            consume(TokenType.IF)
            consume(TokenType.LEFT_PAREN)
            val rules = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            val consequent = blockStatement()
            if (currentToken.type == TokenType.ELSE) {
                consume(TokenType.ELSE)
                return if (currentToken.type == TokenType.IF) {
                    val alternate = ifStatement()
                    Statement.IfStatement(rules, consequent, alternate)
                } else {
                    val alternate = blockStatement()
                    Statement.IfStatement(rules, consequent, alternate)
                }
            }
            return Statement.IfStatement(rules, consequent, null)
        }

        fun forStatement(): Statement.ForStatement {
            consume(TokenType.FOR)
            consume(TokenType.LEFT_PAREN)
            val init = expr.expression()
            consume(TokenType.IN)
            val rule = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            val body = blockStatement()
            return Statement.ForStatement(init, rule, body)
        }

        fun tryStatement(): Statement.TryStatement {
            consume(TokenType.TRY)
            val body = blockStatement()
            consume(TokenType.CATCH)
            consume(TokenType.LEFT_PAREN)
            val expect = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            val catch = blockStatement()
            return Statement.TryStatement(body, expect, catch)
        }

        fun visitorStatement(): Statement.VisitorStatement? {
            val visitor = when (currentToken.type) {
                TokenType.PRIVATE -> {
                    consume(TokenType.PRIVATE)
                    Statement.VisitorType.PRIVATE
                }

                else -> {
                    consume(TokenType.PUBLIC)
                    Statement.VisitorType.PUBLIC
                }
            }
            val state = statements()
            return state?.let { Statement.VisitorStatement(visitor, it) }
        }

        fun importStatement(): Statement.ImportStatement {
            consume(TokenType.IMPORT)
            val file = expr.expression()
            consume(TokenType.SEMICOLON)
            return Statement.ImportStatement(file)
        }

        fun switchStatement(): Statement.SwitchStatement {
            consume(TokenType.SWITCH)
            fun case(): Statement.CaseDeclaration {
                consume(TokenType.CASE)
                consume(TokenType.LEFT_PAREN)
                val rule = expr.expression()
                consume(TokenType.RIGHT_PAREN)
                consume(TokenType.COLON)
                var body: Statement?
                if (currentToken.type == TokenType.LEFT_BRACE) body = blockStatement() else {
                    body = statements()
                    nextToken()
                }
                return Statement.CaseDeclaration(rule, body)
            }
            consume(TokenType.LEFT_PAREN)
            val rule = expr.expression()
            consume(TokenType.RIGHT_PAREN)
            consume(TokenType.LEFT_BRACE)
            val case = ArrayList<Statement.CaseDeclaration>()
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                case.add(case())
            }
            consume(TokenType.RIGHT_BRACE)
            return Statement.SwitchStatement(rule, case)
        }


    }

    private inner class Expressions {
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
            return when (currentToken.type) {
                TokenType.PLUS_EQUAL -> consume(complex[0])
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
            return Expression.AssignmentExpression(left, operator, right)
        }

        fun MemberExpression(): Expression {
            val objectExpr = primaryExpression()
            var expr: Expression = Expression.NullLiteral
            while (currentToken.type == TokenType.DOT || currentToken.type == TokenType.LEFT_SQUARE) {
                expr = if (currentToken.type == TokenType.DOT) {
                    consume(TokenType.DOT)
                    val property = expression()
                    Expression.MemberExpression(objectExpr, property, false)
                } else {
                    consume(TokenType.LEFT_SQUARE)
                    val property = expression()
                    consume(TokenType.RIGHT_SQUARE)
                    Expression.MemberExpression(objectExpr, property, true)
                }
            }
            return expr
        }

        fun CallExpression(): Expression.CallExpression {
            val value: ArrayList<Expression> = arrayListOf()
            val id = Identifier()
            consume(TokenType.LEFT_PAREN)
            while (currentToken.type != TokenType.RIGHT_PAREN) {
                if (currentToken.type == TokenType.COMMA) {
                    consume(currentToken.type)
                }
                value.add(expression())
            }
            consume(TokenType.RIGHT_PAREN)
            return Expression.CallExpression(id, value)
        }


        fun expression(): Expression {

            when (currentToken.type) {
                TokenType.MINUS, TokenType.BANG -> {
                    val operator = currentToken.value
                    nextToken()
                    val right = primaryExpression()
                    return Expression.UnaryExpression(operator, right)
                }

                TokenType.IDENTIFIER -> {
                    if (tokens[currentTokenIndex].type == TokenType.LEFT_PAREN) {
                        return CallExpression()
                    } else if (tokens[currentTokenIndex].type == TokenType.DOT || tokens[currentTokenIndex].type == TokenType.LEFT_SQUARE) {
                        return MemberExpression()
                    }
                    return Identifier()
                }

                else -> {
                    when (tokens[currentTokenIndex].type) {
                        TokenType.Incrementing, TokenType.Subtraction -> {
                            val operator = tokens[currentTokenIndex].value
                            val left = primaryExpression()
                            nextToken()
                            return Expression.UnaryExpression(operator, left)
                        }

                        else -> {
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
                }
            }
        }

        fun typeExpression(): MutableMap<Expression, TokenType> {
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

                TokenType.STRING -> {
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

        fun leftUnaryExpression(): Expression.UnaryExpression {
            val operator = currentToken.value
            nextToken()
            val right = primaryExpression()
            consume(TokenType.SEMICOLON)
            return Expression.UnaryExpression(operator, right)
        }

        fun rightUnaryExpression(): Expression.UnaryExpression {
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
            return when (currentToken.type) {
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

    private inner class Literal {
        val literal: Expression = when (currentToken.type) {
            TokenType.NUMBER -> NumericLiteral()
            TokenType.STRING -> StringLiteral()
            TokenType.FALSE, TokenType.TRUE -> BooleanLiteral()
            TokenType.NULL -> NullLiteral()
            TokenType.OBJECT -> ObjectLiteral()
            else -> {
                thrower.SyntaxError("Literal: unexpected literal production")
                Expression.NullLiteral
            }
        }
        val isLiteral: Boolean =
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

        fun StringLiteral(): Expression.StringLiteral {
            return Expression.StringLiteral(consume(TokenType.STRING).value)
        }

        fun ObjectLiteral(): Expression.ObjectLiteral {
            return Expression.ObjectLiteral(consume(currentToken.type).value)
        }

        fun NullLiteral(): Expression.NullLiteral {
            consume(TokenType.NULL)
            return Expression.NullLiteral
        }
    }

    private fun peek(): Token? {
        val nextIndex = currentTokenIndex + 1
        return if (nextIndex < tokens.size) tokens[nextIndex] else null
    }

    private fun check(token: TokenType): Boolean {
        return peek()?.type == token
    }

    private fun nextToken(): Token {
        currentToken =
            if (currentTokenIndex < tokens.size || currentToken.type != TokenType.EOF) tokens[currentTokenIndex++] else {
                isEnd = true
                if (currentToken.type == TokenType.EOF) isEnd = true
                currentToken
            }
        return currentToken
    }

    private fun consume(token: TokenType, error: String): Token {
        val ctoken = currentToken
        if (currentToken.type != token) {
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