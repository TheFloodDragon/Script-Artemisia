package net.mugwort.mscript.ast.token

/**
 * TokenType枚举类定义了所有可能的Token类型。
 */
enum class TokenType(val id: String) {
    // Keywords
    CLASS("class"),
    CONST("const"),
    VAL("val"),
    LET("let"),
    OR("||"),
    BREAK("break"),
    CONTINUE("continue"),
    ELSE("else"),
    FALSE("false"),
    VAR("var"),
    FOR("for"),
    IF("if"),
    INCLUDE("include"),
    NULL("null"),
    WHILE("while"),
    TRUE("true"),
    PRINTLN("println"),
    PRINT("print"),

    // Types
    RETURN("return"),
    IDENTIFIER("identifier"),
    NUMBER("number"),
    STRING("string"),
    BOOLEAN("boolean"),
    VOID("Void"),
    OBJECT("object"),

    // Symbols
    ARROW(">"),
    COLON(":"),
    AND("&&"),
    MINUS_EQUAL("-="),
    PLUS_EQUAL("+="),
    STAR_EQUAL("*="),
    SLASH_EQUAL("/="),
    MODULUS_EQUAL("%="),
    MODULUS("%"),
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    COMMA(","),
    DOT("."),
    MINUS("-"),
    PLUS("+"),
    SEMICOLON(";"),
    SLASH("/"),
    STAR("*"),
    LEFT_SQUARE("["),
    RIGHT_SQUARE("]"),

    // Comparison
    BANG("!"),
    BANG_EQUAL("!="),
    EQUAL("="),
    EQUAL_EQUAL("=="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),

    DO("do"),
    SUPER("super"),
    THIS("this"),
    LOGICAL_NOT("!"),
    // EOF
    EOF("eof");

    companion object {
        /**
         * 定义复合赋值运算符列表
         * += ; -= ; *= ; /= ; %=
         * @see TokenType
         */
        val complex = listOf(MINUS_EQUAL, PLUS_EQUAL, STAR_EQUAL, SLASH_EQUAL, MODULUS_EQUAL)

        /**
         * 定义等式运算符列表
         * == ; !=
         * @see TokenType
         */
        val equalityOperators = listOf(EQUAL, BANG_EQUAL)
        /**
         * List of relational operators.
         * @see TokenType
         */
        val relationalOperators = listOf(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)

        /**
         * List of binary operators.
         * @see TokenType
         */
        val binaryOperators = listOf(PLUS, MINUS,STAR, SLASH, MODULUS)

        /**
         * 根据id获取对应的TokenType
         * @param id Token类型的标识符
         * @return 对应的TokenType，如果不存在则返回null
         */

        /**
         * 根据id获取对应的TokenType
         * @param id Token类型的标识符
         * @return 对应的TokenType，如果不存在则返回null
         */
        @JvmStatic
        fun fromId(id: String?): TokenType? {
            return values().firstOrNull { it.id == id }
        }
    }
}