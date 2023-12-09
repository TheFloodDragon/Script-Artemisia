package net.mugwort.mscript.core.ast.token

/**
 * TokenType枚举类定义了所有可能的Token类型。
 */
enum class TokenType(val id: String) {
    // Keywords
    CLASS("class"),
    CONST("const"),


    PUBLIC("public"),
    PRIVATE("private"),

    SWITCH("switch"),
    CASE("case"),
    VAL("val"),
    LET("let"),
    OR("||"),
    TRY("try"),
    CATCH("catch"),
    IN("in"),
    BREAK("break"),
    CONTINUE("continue"),
    ELSE("else"),
    FALSE("false"),
    VAR("var"),
    FOR("for"),
    IF("if"),
    IMPORT("import"),
    NULL("null"),
    WHILE("while"),
    TRUE("true"),

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
    Incrementing("++"),
    Subtraction("--"),
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