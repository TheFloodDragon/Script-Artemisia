package net.artemisia.compiler

import net.artemisia.api.expection.thrower
import net.artemisia.core.ast.core.Expression
import net.artemisia.core.ast.core.Statement
import net.artemisia.core.ast.token.BigLocation
import net.artemisia.core.ast.token.Location
import net.artemisia.core.ast.token.Token
import net.artemisia.core.ast.token.TokenType
import net.artemisia.utils.JsonUtils
import java.io.File

class Parser(code: String, val file: File) {
    // 定义一个变量index，初始值为0
    private var index = 0

    // 定义一个变量tokens，初始值为Lexer函数的返回值
    private val tokens = Lexer(code).tokens

    // 定义一个变量currentToken，初始值为tokens数组的第一个元素
    private var currentToken = tokens[index]

    // 定义一个变量isEnd，初始值为currentToken的类型是否为TokenType.EOF或者index是否大于tokens的长度
    private var isEnd = currentToken.type == TokenType.EOF || index >= tokens.size

    // 定义一个变量statementList，初始值为一个可变列表
    private val statementList: MutableList<Statement> = mutableListOf()

    // 定义一个变量expr，初始值为expression函数的返回值
    private val expr = expression()

    // 定义一个变量state，初始值为statement函数的返回值
    private val state = statement()

    // 定义一个变量line，初始值为currentToken的位置行
    private var line = currentToken.location.line

    // 定义一个变量column，初始值为currentToken的位置列
    private var column = currentToken.location.column


    fun parser(): Statement.Program {
        // 当不是结束时
        while (!isEnd) {
            // 如果thrower.endProcess为真
            if (thrower.endProcess) {
                // 获取行号
                val end = Location(line + 1, 1)
                // 返回一个程序，其中包含statementList和BigLocation
                return Statement.Program(statementList, BigLocation(Location(1, 1), end))
            }
            // 如果state不为空，则将state添加到statementList中
            state.get()?.let { statementList.add(it) }
        }
        // 获取行号
        val end = Location(line + 1, 1)
        // 返回一个程序，其中包含statementList和BigLocation
        return Statement.Program(statementList, BigLocation(Location(1, 1), end))
    }

    // 返回解析后的json字符串
    fun parserJson(): String {
        // 返回解析后的json字符串
        return JsonUtils.toJson(parser().toMap())
    }

    // 打印编码

    private inner class statement {
        fun get(): Statement? {
            //根据当前token的类型，返回不同的语句
            return when (currentToken.type) {
                TokenType.VAR -> varStatement()
                TokenType.CONST, TokenType.VAL -> varStatement(true)
                TokenType.LEFT_BRACE -> blockStatement()
                TokenType.RETURN -> returnStatement()
                TokenType.WHILE -> whileStatement()
                TokenType.DO -> doWhileStatement()
                TokenType.CLASS -> classStatement()
                TokenType.IF -> ifStatement()
                TokenType.TRY -> tryStatement()
                TokenType.PRIVATE, TokenType.PUBLIC, TokenType.PROTECTED, TokenType.ALREADY -> visitorStatement()
                TokenType.ENUM -> enumStatement()
                TokenType.EVENT -> eventStatement()
                TokenType.IMPORT -> importStatement()
                TokenType.SWITCH -> switchStatement()
                TokenType.DEF -> functionStatement()
                TokenType.SEMICOLON -> {
                    //当前token是分号，返回空语句
                    Statement.EmptyStatement(BigLocation(Location(line, column), Location(line, column)))
                }

                TokenType.IDENTIFIER -> {
                    //当前token是标识符，返回表达式语句
                    val start = Location(line, column)
                    when (peek().type) {
                        TokenType.LEFT_PAREN -> {
                            //当前token是左括号，返回表达式语句
                            val end = getEnd()
                            var ret = Statement.ExpressionStatement(expr.callee(), BigLocation(start, end))
                            if (currentToken.type == TokenType.DOT) {
                                //当前token是点，返回属性访问表达式语句
                                ret =
                                    Statement.ExpressionStatement(expr.member(ret.expression), BigLocation(start, end))
                                spilt()
                                return ret
                            }
                            ret
                        }

                        TokenType.DOT, TokenType.LEFT_SQUARE -> {
                            //当前token是点或者左方括号，返回属性访问表达式语句
                            val end = getEnd()
                            Statement.ExpressionStatement(expr.member(), BigLocation(start, end))
                        }

                        TokenType.Incrementing, TokenType.Subtraction -> {
                            //当前token是自增自减，返回表达式语句
                            val end = getEnd()
                            expr.unary()?.let { Statement.ExpressionStatement(it, BigLocation(start, end)) }
                        }

                        else -> {
                            //当前token不是自增自减，点，左括号，返回表达式语句
                            val end = getEnd()
                            if (expr.logical.contains(peek().type)) {
                                //当前token是逻辑运算符，返回逻辑表达式语句
                                Statement.ExpressionStatement(expr.logical(expr.get()), BigLocation(start, end))
                            } else if (expr.binary.contains(peek().type)) {
                                //当前token是二元运算符，返回二元表达式语句
                                Statement.ExpressionStatement(expr.binary(expr.get()), BigLocation(start, end))
                            } else {
                                //当前token是赋值运算符，返回赋值表达式语句
                                Statement.ExpressionStatement(expr.assignment(), BigLocation(start, end))
                            }
                        }
                    }
                }

                TokenType.MINUS, TokenType.BANG, TokenType.Incrementing, TokenType.Subtraction -> {
                    //当前token是减号，感叹号，自增自减，返回表达式语句
                    val loc = Location(line, column)
                    expr.unary()?.let { Statement.ExpressionStatement(it, BigLocation(loc, loc)) }
                }

                TokenType.NEWLINE -> {
                    //当前token是换行，返回空语句
                    consume(TokenType.NEWLINE)
                    return null
                }

                else -> {
                    //当前token不是换行，返回表达式语句
                    val start = Location(line, column)
                    return if (currentToken.type != TokenType.EOF) {
                        val end = getEnd()
                        Statement.ExpressionStatement(expr.get(), BigLocation(start, end))
                    } else {
                        null
                    }
                }
            }
        }


        // 函数enumStatement()返回一个Statement.EnumStatement类型的值
        fun enumStatement(): Statement.EnumStatement {
            // 获取当前token的位置
            val start = Location(line, column)
            // 消费当前token，如果当前token的类型不是ENUM，则抛出异常
            consume(TokenType.ENUM)
            // 获取当前token的标识符
            val id = expression().identifier()
            // 创建一个ArrayList用于存储枚举值
            val enums = ArrayList<Expression>()
            // 消费当前token，如果当前token的类型不是LEFT_BRACE，则抛出异常
            consume(TokenType.LEFT_BRACE)
            // 循环检查当前token的类型
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                // 如果当前token的类型是COMMA，则消费当前token
                if (currentToken.type == TokenType.COMMA) {
                    consume(TokenType.COMMA)
                }
                // 根据当前token的类型，进行不同的操作
                when (currentToken.type) {
                    TokenType.IDENTIFIER -> {
                        // 如果当前token的类型在expr.complex中，或者当前token的类型是EQUAL，则获取当前token的标识符，并将其添加到enums中
                        if (expr.complex.contains(peek().type) || check(TokenType.EQUAL)) {
                            val left = expr.get()
                            var op = ""
                            // 如果当前token的类型不在expr.complex中，则获取当前token的赋值操作符
                            if (!expr.complex.contains(currentToken.type)) {
                                op = expr.isAssignmentOperator().value
                            } else {
                                // 否则抛出异常
                                thrower.send(
                                    "Assignment in Enumerations are only accepted EQUAL",
                                    "Enumerations Error",
                                    file,
                                    BigLocation(currentToken.location, currentToken.location)
                                )
                            }
                            // 获取当前token的右值
                            val right = expr.get()
                            // 将当前token的标识符和赋值操作符以及右值添加到enums中
                            enums.add(Expression.AssignmentExpression(left, op, right))
                        } else if (!check(TokenType.RIGHT_BRACE) || !check(TokenType.COMMA)) {
                            // 如果当前token的类型不在RIGHT_BRACE和COMMA中，则抛出异常
                            thrower.send(
                                "Enumerations are only accepted Assignment and Identifier",
                                "Enumerations Error",
                                file,
                                BigLocation(currentToken.location, currentToken.location)
                            )
                        } else {
                            // 否则，将当前token的标识符添加到enums中
                            enums.add(expr.identifier())
                        }
                    }

                    TokenType.NEWLINE -> {
                        // 如果当前token的类型是NEWLINE，则调用spilt()函数
                        spilt()
                    }

                    else -> {
                        // 否则，抛出异常
                        thrower.send(
                            "Enumerations are only accepted Assignment and Identifier",
                            "Enumerations Error",
                            file,
                            BigLocation(currentToken.location, currentToken.location)
                        )
                    }
                }
                // 如果当前token的类型是RIGHT_BRACE，则跳出循环
                if (currentToken.type == TokenType.RIGHT_BRACE) break
            }
            // 消费当前token，如果当前token的类型不是RIGHT_BRACE，则抛出异常
            consume(TokenType.RIGHT_BRACE)
            // 获取当前token的位置
            val end = getEnd()
            // 返回一个Statement.EnumStatement类型的值
            return Statement.EnumStatement(id, enums, BigLocation(start, end))
        }

        // 获取访问者声明
        fun visitorStatement(): Statement.VisitorStatement {
            // 获取当前行和列
            val start = Location(line, column)
            // 根据当前token类型，获取访问者类型
            val visitor = when (currentToken.type) {
                TokenType.PUBLIC -> {
                    // 消费当前token
                    consume(currentToken.type)
                    // 返回public访问者类型
                    Statement.VisitorType.PUBLIC
                }

                TokenType.ALREADY -> {
                    // 消费当前token
                    consume(currentToken.type)
                    // 返回already访问者类型
                    Statement.VisitorType.ALREADY
                }

                TokenType.PROTECTED -> {
                    // 消费当前token
                    consume(currentToken.type)
                    // 返回protected访问者类型
                    Statement.VisitorType.PROTECTED
                }

                TokenType.PRIVATE -> {
                    // 消费当前token
                    consume(currentToken.type)
                    // 返回private访问者类型
                    Statement.VisitorType.PRIVATE
                }

                else -> {
                    // 消费当前token
                    consume(currentToken.type)
                    // 返回public访问者类型
                    Statement.VisitorType.PUBLIC
                }
            }
            // 获取当前状态
            val state = this.get()
            // 获取结束行和列
            val end = getEnd()
            // 返回访问者声明
            return Statement.VisitorStatement(visitor, state, BigLocation(start, end))
        }


        fun eventStatement(): Statement.EventStatement {
            //获取当前行和列
            val start = Location(line, column)
            //消耗EVENT token
            consume(TokenType.EVENT)
            //获取标识符
            val id = expr.identifier()
            //获取块语句
            val body = blockStatement()
            //获取结束行和列
            val end = getEnd()
            //返回事件语句
            return Statement.EventStatement(id, body, BigLocation(start, end))
        }

        //获取参数
        fun paramGetter(): ArrayList<Statement.VariableStatement> {
            //创建一个参数列表
            val params = ArrayList<Statement.VariableStatement>()
            //消耗左括号
            consume(TokenType.LEFT_PAREN)
            //循环
            while (currentToken.type != TokenType.RIGHT_PAREN) {
                //消耗逗号
                if (currentToken.type == TokenType.COMMA) {
                    consume(TokenType.COMMA)
                }
                //根据当前token的类型
                when (currentToken.type) {
                    //如果是const或者val
                    TokenType.CONST, TokenType.VAL -> params.add(varStatement(true, isParams = true))
                    //如果是var
                    TokenType.VAR -> params.add(varStatement(false, isParams = true))
                    //如果是标识符
                    else -> {
                        //如果当前token的类型是标识符
                        if (currentToken.type == TokenType.IDENTIFIER) {
                            //添加变量语句
                            params.add(varStatement(false, isParams = true))
                        }
                    }
                }
            }
            //消耗右括号
            consume(TokenType.RIGHT_PAREN)
            //返回参数列表
            return params
        }

        //变量语句
        fun varStatement(isConst: Boolean = false, isParams: Boolean = false): Statement.VariableStatement {
            //获取当前行和列
            val start = Location(line, column)

            //声明
            fun declaration(): Statement.VariableDeclaration {
                //获取标识符
                val id = expr.identifier()
                //如果当前token的类型是冒号
                if (currentToken.type == TokenType.COLON) {
                    //消耗冒号
                    consume(TokenType.COLON)
                    //如果当前token的类型是const且当前token的类型不是等于
                    if (isConst && !check(TokenType.EQUAL)) {
                        println(currentToken)
                        //抛出错误
                        thrower.send(
                            "Constants must be initialized",
                            "NotInitializedError",
                            file,
                            BigLocation(currentToken.location, currentToken.location)
                        )
                    } else {

                        //获取类型
                        val init = expr.typeGetter()

                        //如果当前token的类型是等于
                        if (currentToken.type == TokenType.EQUAL) {
                            //消耗等于
                            consume(TokenType.EQUAL)
                            //如果类型不是指定类型且类型不是对象
                            if (init.values.first() != currentToken.type && init.values.first() != TokenType.OBJECT) {
                                //抛出错误
                                thrower.send(
                                    "Non-specified type",
                                    "SpecifiedTypeError",
                                    file,
                                    BigLocation(currentToken.location, currentToken.location)
                                )
                            }
                            //获取初始化
                            val inits = expr.primary()
                            //返回变量声明
                            return Statement.VariableDeclaration(id, inits)
                        }
                        //返回变量声明
                        return Statement.VariableDeclaration(id, init.keys.first())
                    }
                }
                //如果当前token的类型是const且当前token的类型不是等于
                if (isConst && currentToken.type != TokenType.EQUAL) {
                    //抛出错误
                    thrower.send(
                        "Constants must be initialized",
                        "NotInitializedError",
                        file,
                        BigLocation(currentToken.location, currentToken.location)
                    )
                } else {
                    //消耗等于
                    consume(TokenType.EQUAL)
                }

                //获取初始化
                val init = expr.get()
                //返回变量声明
                return Statement.VariableDeclaration(id, init)
            }
            //如果不是参数
            if (!isParams) {
                //如果当前token的类型是const
                if (isConst) {
                    //如果当前token的类型是const
                    if (currentToken.type == TokenType.CONST) {
                        //消耗const
                        consume(TokenType.CONST)
                        //如果当前token的类型是val
                        if (currentToken.type == TokenType.VAL) {
                            //消耗val
                            consume(TokenType.VAL)
                        }
                    } else {
                        //消耗val
                        consume(TokenType.VAL)
                    }
                } else {
                    //消耗var
                    consume(TokenType.VAR)
                }
            } else {
                //如果当前token的类型是const
                if (isConst) {
                    //如果当前token的类型是const
                    if (currentToken.type == TokenType.CONST) {
                        //消耗const
                        consume(TokenType.CONST)
                        //如果当前token的类型是val
                        if (currentToken.type == TokenType.VAL) {
                            //消耗val
                            consume(TokenType.VAL)
                        }
                    }

                } else {
                    //如果当前token的类型是var
                    if (currentToken.type == TokenType.VAR) {
                        //消耗var
                        consume(TokenType.VAR)
                    }
                }
            }
            //获取声明
            val declaration = declaration()

            //如果当前token的类型是逗号且是参数
            if (currentToken.type == TokenType.COMMA && isParams) {
                //消耗逗号
                consume(TokenType.COMMA)
            } else if (currentToken.type == TokenType.RIGHT_PAREN) {
                //获取结束行和列
                val end = getEnd()
                //返回变量语句
                return Statement.VariableStatement(declaration, isConst, BigLocation(start, end))
            } else {
                //分割
                spilt()
            }
            //获取结束行和列
            val end = getEnd()
            //返回变量语句
            return Statement.VariableStatement(declaration, isConst, BigLocation(start, end))
        }

        // 函数blockStatement()返回一个Statement.BlockStatement，该函数用于解析块语句
        fun blockStatement(): Statement.BlockStatement {
            // 获取当前行号和列号
            val start = Location(line, column)
            // 解析左大括号
            consume(TokenType.LEFT_BRACE)
            // 创建一个存放语句的ArrayList
            val statements = ArrayList<Statement>()
            // 当当前token的类型不是右大括号时，循环获取语句
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                // 将当前token添加到statements中
                get()?.let { statements.add(it) }
            }
            // 解析右大括号
            consume(TokenType.RIGHT_BRACE)
            // 获取结束行号和列号
            val end = getEnd()
            // 返回一个Statement.BlockStatement，该语句包含statements和BigLocation(start,end)
            return Statement.BlockStatement(statements, BigLocation(start, end))
        }

        // 函数声明
        fun functionStatement(): Statement.FunctionDeclaration {
            // 获取当前行和列
            val start = Location(line, column)
            // 消费DEF token
            consume(TokenType.DEF)
            // 获取标识符
            val id = expr.identifier()
            // 获取参数
            val params = paramGetter()
            // 获取返回值
            val returnValue = if (currentToken.type == TokenType.COLON) {
                // 如果当前token类型为COLON，则消费COLON token，获取类型
                consume(TokenType.COLON)
                expr.typeGetter().keys.first()
            } else {
                // 否则返回一个空对象
                Expression.ObjectLiteral(null)
            }
            // 如果当前token类型为LEFT_BRACE，则消费LEFT_BRACE token，获取块语句
            if (currentToken.type == TokenType.LEFT_BRACE) {
                val body: Statement.BlockStatement = blockStatement()
                // 获取结束行和列
                val end = getEnd()
                // 返回函数声明
                return Statement.FunctionDeclaration(id, params, returnValue, body, BigLocation(start, end))
            }
            // 获取结束行和列
            val end = getEnd()
            // 返回函数声明
            return Statement.FunctionDeclaration(id, params, returnValue, null, BigLocation(start, end))
        }

        // 返回语句
        fun returnStatement(): Statement.ReturnStatement {
            // 获取当前行号和列号
            val start = Location(line, column)
            // 消费return关键字
            consume(TokenType.RETURN)
            // 获取表达式
            val argument: Expression = expr.get()
            // 分割
            spilt()
            // 获取结束行号和列号
            val end = getEnd()
            // 返回返回语句
            return Statement.ReturnStatement(argument, BigLocation(start, end))
        }

        fun whileStatement(): Statement.WhileStatement {
            val start = Location(line, column)
            consume(TokenType.WHILE)
            consume(TokenType.LEFT_PAREN)
            val rule = expr.get()
            consume(TokenType.RIGHT_PAREN)
            val body = blockStatement()
            val end = getEnd()
            return Statement.WhileStatement(rule, body, BigLocation(start, end))
        }

        fun doWhileStatement(): Statement.DoWhileStatement {
            val start = Location(line, column)
            consume(TokenType.DO)
            val body = blockStatement()
            consume(TokenType.WHILE)
            consume(TokenType.LEFT_PAREN)
            val rule = expr.get()
            consume(TokenType.RIGHT_PAREN)
            val end = getEnd()
            return Statement.DoWhileStatement(body, rule, BigLocation(start, end))
        }

        fun classStatement(): Statement.ClassDeclaration {
            val start = Location(line, column)
            consume(TokenType.CLASS)
            val id = expr.identifier()
            if (currentToken.type == TokenType.LEFT_PAREN) {
                val params = paramGetter()
                val body = blockStatement()
                val end = getEnd()
                return Statement.ClassDeclaration(id, params, body, BigLocation(start, end))
            }
            val body = blockStatement()
            val end = getEnd()
            return Statement.ClassDeclaration(id, null, body, BigLocation(start, end))
        }

        fun ifStatement(): Statement.IfStatement {
            val start = Location(line, column)
            consume(TokenType.IF)
            consume(TokenType.LEFT_PAREN)
            val rules = expr.get()

            consume(TokenType.RIGHT_PAREN)
            val consequent = blockStatement()
            if (currentToken.type == TokenType.ELSE) {
                consume(TokenType.ELSE)
                return if (currentToken.type == TokenType.IF) {
                    val alternate = ifStatement()
                    val end = getEnd()
                    Statement.IfStatement(rules, consequent, alternate, BigLocation(start, end))
                } else {
                    val alternate = blockStatement()
                    val end = getEnd()
                    Statement.IfStatement(rules, consequent, alternate, BigLocation(start, end))
                }
            }
            val end = getEnd()
            return Statement.IfStatement(rules, consequent, null, BigLocation(start, end))
        }

        fun tryStatement(): Statement.TryStatement {
            val start = Location(line, column)
            consume(TokenType.TRY)
            val body = blockStatement()
            if (currentToken.type == TokenType.FINALLY) {
                consume(TokenType.FINALLY)
                val finally = blockStatement()
                val end = getEnd()
                return Statement.TryStatement(body, null, null, finally, BigLocation(start, end))
            } else {
                consume(TokenType.CATCH)
                consume(TokenType.LEFT_PAREN)
                val expect = expr.get()
                consume(TokenType.RIGHT_PAREN)
                val catch = blockStatement()
                if (currentToken.type == TokenType.FINALLY) {
                    consume(TokenType.FINALLY)
                    val finally = blockStatement()
                    val end = getEnd()
                    return Statement.TryStatement(body, expect, catch, finally, BigLocation(start, end))
                }
                val end = getEnd()
                return Statement.TryStatement(body, expect, catch, null, BigLocation(start, end))
            }
        }

        fun importStatement(): Statement.ImportStatement {
            val start = Location(line, column)
            consume(TokenType.IMPORT)
            val file = expr.get()
            spilt()
            val end = getEnd()
            return Statement.ImportStatement(file, BigLocation(start, end))
        }

        fun switchStatement(): Statement.SwitchStatement {
            val start = Location(line, column)
            consume(TokenType.SWITCH)
            fun case(): Statement.CaseDeclaration {
                val location = Location(line, column)
                consume(TokenType.CASE)
                consume(TokenType.LEFT_PAREN)
                val rule = expr.get()
                consume(TokenType.RIGHT_PAREN)
                consume(TokenType.COLON)
                val body: Statement?
                if (currentToken.type == TokenType.LEFT_BRACE) body = blockStatement() else {
                    body = get()
                    advance()
                }
                val end = getEnd()
                return Statement.CaseDeclaration(rule, body, BigLocation(location, end))
            }
            consume(TokenType.LEFT_PAREN)
            val rule = expr.get()
            consume(TokenType.RIGHT_PAREN)
            consume(TokenType.LEFT_BRACE)
            val case = ArrayList<Statement.CaseDeclaration>()
            while (currentToken.type != TokenType.RIGHT_BRACE) {
                case.add(case())
            }
            consume(TokenType.RIGHT_BRACE)
            val end = getEnd()
            return Statement.SwitchStatement(rule, case, BigLocation(start, end))
        }

    }

    private inner class expression {
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
        val unary = listOf(
            TokenType.BANG,
            TokenType.MINUS,
            TokenType.Incrementing,
            TokenType.Subtraction
        )
        val logical = listOf(
            TokenType.LESS_EQUAL,
            TokenType.BANG_EQUAL,
            TokenType.GREATER,
            TokenType.GREATER_EQUAL,
            TokenType.LESS,
            TokenType.EQUAL_EQUAL
        )

        fun get(): Expression {
            return when (currentToken.type) {
                TokenType.MINUS, TokenType.BANG, TokenType.Incrementing, TokenType.Subtraction -> unary()!!
                TokenType.IDENTIFIER -> {

                    if (check(TokenType.LEFT_PAREN)) {
                        val left = callee()
                        if (binary.contains(currentToken.type)) {
                            return binary(left)
                        }
                        if (logical.contains(currentToken.type)) {
                            return logical(left)
                        }
                        return left
                    }
                    if (check(TokenType.DOT) || check(TokenType.LEFT_SQUARE)) {
                        val left = member()
                        if (binary.contains(currentToken.type)) {
                            return binary(left)
                        }
                        if (logical.contains(currentToken.type)) {
                            return logical(left)
                        }
                        return left
                    }
                    if (binary.contains(peek().type)) {
                        val left = primary()
                        return binary(left)
                    } else if (logical.contains(peek().type)) {
                        val left = primary()
                        return logical(left)
                    } else if (check(TokenType.Incrementing) || check(TokenType.Subtraction)) {
                        return unary()!!
                    }
                    identifier()
                }

                else -> {
                    if (check(TokenType.DOT) || check(TokenType.LEFT_SQUARE)) {
                        return member()
                    }
                    val left = primary()
                    if (binary.contains(currentToken.type)) {
                        return binary(left)
                    }
                    if (logical.contains(currentToken.type)) {
                        return logical(left)
                    }
                    return left
                }
            }
        }

        fun getLogical() {

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

        fun logical(left: Expression, unAdvance: Boolean = false): Expression.LogicalExpression {
            val operator = currentToken.value
            advance()
            if (peek().type == TokenType.OR || peek().type == TokenType.AND) {
                val right = get()
                val ret = Expression.LogicalExpression(operator, left, right)
                return logical(ret)
            }
            val right = get()
            return Expression.LogicalExpression(operator, left, right)
        }

        fun unary(): Expression.UnaryExpression? {
            if (unary.contains(currentToken.type)) {
                val operator = currentToken.value
                advance()
                val right = get()
                spilt()
                return Expression.UnaryExpression(operator, right, true)
            } else if (check(TokenType.Subtraction) || check(TokenType.Incrementing)) {
                val operator = peek().value
                val left = get()
                advance()
                spilt()
                return Expression.UnaryExpression(operator, left, false)
            }
            return null
        }

        fun binary(left: Expression): Expression.BinaryExpression {
            val op = currentToken.value
            advance()
            val right = get()
            return Expression.BinaryExpression(op, left, right)
        }

        fun assignment(): Expression.AssignmentExpression {
            val left = get()
            val operator = isAssignmentOperator().value
            val right = get()
            spilt()
            return Expression.AssignmentExpression(left, operator, right)
        }

        fun callee(): Expression.CallExpression {
            val start = Location(line, column)
            val value: ArrayList<Expression> = arrayListOf()
            val id = identifier()
            consume(TokenType.LEFT_PAREN)
            while (currentToken.type != TokenType.RIGHT_PAREN) {
                if (currentToken.type == TokenType.COMMA) {
                    consume(currentToken.type)
                }
                value.add(get())
            }
            consume(TokenType.RIGHT_PAREN)
            val end = getEnd()
            return Expression.CallExpression(id, value, BigLocation(start, end))
        }

        fun member(expr: Expression? = null): Expression {
            var objectExpr = expr ?: primary()

            while (currentToken.type == TokenType.DOT || currentToken.type == TokenType.LEFT_SQUARE) {
                objectExpr = if (currentToken.type == TokenType.DOT) {
                    consume(TokenType.DOT)
                    val property = get()
                    Expression.MemberExpression(objectExpr, property, false)
                } else {
                    consume(TokenType.LEFT_SQUARE)
                    val property = get()
                    consume(TokenType.RIGHT_SQUARE)
                    Expression.MemberExpression(objectExpr, property, true)
                }
            }

            return objectExpr
        }

        fun primary(): Expression {
            return when (currentToken.type) {
                TokenType.IDENTIFIER -> {
                    return identifier()
                }

                TokenType.LEFT_PAREN -> {
                    consume(TokenType.LEFT_PAREN)
                    val ret = Expression.GroupExpression(get())
                    consume(TokenType.RIGHT_PAREN)
                    ret
                }

                else -> Literal().literal
            }
        }

        fun typeGetter(): MutableMap<Expression, TokenType> {
            return when (currentToken.type) {
                TokenType.IDENTIFIER -> {
                    val identifier = Expression.Identifier(currentToken.value)
                    advance()
                    mutableMapOf(identifier to TokenType.IDENTIFIER)
                }

                TokenType.NUMBER -> {
                    val number = Expression.NumericLiteral(null)
                    advance()
                    mutableMapOf(number to TokenType.NUMBER)
                }

                TokenType.STRING -> {
                    val string = Expression.StringLiteral(null)
                    advance()
                    mutableMapOf(string to TokenType.STRING)
                }

                TokenType.BOOLEAN -> {
                    val boolean = Expression.BooleanLiteral(null)
                    advance()
                    mutableMapOf(boolean to TokenType.BOOLEAN)
                }

                TokenType.OBJECT -> {
                    val obj = Expression.ObjectLiteral(null)
                    advance()
                    mutableMapOf(obj to TokenType.OBJECT)
                }

                TokenType.VOID -> {
                    val void = Expression.VoidLiteral
                    advance()
                    mutableMapOf(void to TokenType.VOID)
                }

                else -> {
                    thrower.send(
                        "Unknown Type ['${currentToken.value}']",
                        "InvalidExpression",
                        file,
                        BigLocation(currentToken.location, currentToken.location)
                    )
                    mutableMapOf(Expression.NullLiteral to TokenType.NULL)
                }
            }
        }

        fun identifier(): Expression.Identifier {
            return Expression.Identifier(consume(TokenType.IDENTIFIER).value)
        }
    }

    inner class Literal {
        val literal: Expression = when (currentToken.type) {
            TokenType.NUMBER -> NumericLiteral()
            TokenType.STRING -> StringLiteral()
            TokenType.FALSE, TokenType.TRUE -> BooleanLiteral()
            TokenType.NULL -> NullLiteral()
            TokenType.OBJECT -> ObjectLiteral()
            TokenType.VOID -> VoidLiteral()
            else -> {
                thrower.send(
                    "Unexpected literal production ['${currentToken.value}']",
                    "LiteralError",
                    file,
                    BigLocation(currentToken.location, currentToken.location)
                )

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

        fun VoidLiteral(): Expression.VoidLiteral {
            consume(TokenType.VOID)
            return Expression.VoidLiteral
        }

        fun NullLiteral(): Expression.NullLiteral {
            consume(TokenType.NULL)
            return Expression.NullLiteral
        }
    }

    private fun peek(): Token {
        return if (!isEnd) tokens[index + 1] else currentToken
    }

    private fun past(): Token {
        return tokens[index - 1]
    }

    private fun check(type: TokenType): Boolean {
        return peek().type == type
    }

    private fun advance(): Token {
        index += 1
        currentToken = if (index < tokens.size) tokens[index] else currentToken
        line = currentToken.location.line
        column = currentToken.location.column
        if (currentToken.type == TokenType.EOF || index >= tokens.size) isEnd = true
        return currentToken
    }

    private fun consume(tokenType: TokenType, error: String): Token {
        val token = currentToken
        if (currentToken.type != tokenType) {
            thrower.SyntaxError(error)
        }
        advance()
        return token
    }

    private fun spilt() {
        if (currentToken.type != TokenType.NEWLINE && currentToken.type != TokenType.EOF) consume(TokenType.SEMICOLON)
        else if (currentToken.type != TokenType.SEMICOLON && currentToken.type != TokenType.EOF && currentToken.type != TokenType.NEWLINE) consume(
            TokenType.SEMICOLON
        )
        else if (currentToken.type != TokenType.EOF && currentToken.type != TokenType.NEWLINE) consume(TokenType.SEMICOLON)
        else if (currentToken.type == TokenType.NEWLINE) consume(TokenType.NEWLINE)
        else if (currentToken.type == TokenType.SEMICOLON) consume(TokenType.SEMICOLON)
    }

    private fun getEnd(): Location {
        return if (check(TokenType.NEWLINE)) {
            Location(line, column)
        } else {
            Location(line, column - 1)
        }
    }

    private fun consume(tokenType: TokenType): Token {
        val token = currentToken
        if (currentToken.type != tokenType) {
            thrower.send(
                "Expect Token ['${tokenType.id}'] but is ['${token.type.id}']",
                "TokenNotFound",
                file,
                BigLocation(token.location, token.location)
            )

            //thrower.SyntaxError("Expect Token -> [\"${tokenType.id}\"] but not Found? Just Found [\"${currentToken.value}\"]")
        }
        advance()
        return token
    }
}