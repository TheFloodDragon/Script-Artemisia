package net.mugwort.mscript.core.ast.token

/**
 * TokenType枚举类定义了所有可能的Token类型。
 */
enum class TokenType(val id: String) {
    // Keywords
    CLASS("class"),
    CONST("const"),
    SWITCH("switch"),
    CASE("case"),
    VAL("val"),
    DEF("def"),
    TRY("try"),
    EVENT("event"),
    CATCH("catch"),
    FINALLY("finally"),
    IN("in"),
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
    RETURN("return"),
    ENUM("enum"),


    //VISITOR
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    ALREADY("already"),


    // Types

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
    OR("||"),
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
    NEWLINE("\n"),

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
            return entries.firstOrNull { it.id == id }
        }
    }
}