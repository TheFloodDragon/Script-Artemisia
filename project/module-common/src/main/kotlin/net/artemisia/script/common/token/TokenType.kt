package net.artemisia.script.common.token


enum class TokenType(val id: String) {
    // Keywords
    MODULE("module"),
    MODIFIER("modifier"),
    CLASS("class"),
    IMPL("impl"),
    EXT("ext"),
    CONSTRUCTOR("constructor"),
    FINAL("final"),
    SWITCH("switch"),
    CASE("case"),
    LET("let"),
    METHOD("method"),
    TRY("try"),
    EVENT("event"),
    CATCH("catch"),
    FINALLY("finally"),
    IN("in"),
    BREAK("break"),
    CONTINUE("continue"),
    ELSE("else"),
    FOR("for"),
    IF("if"),
    IMPORT("import"),

    WHILE("while"),
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    ALREADY("already"),
    RETURN("return"),
    ENUM("enum"),

    NULL("null"),
    FALSE("false"),
    TRUE("true"),
    //VISITOR


    // Types

    IDENTIFIER("identifier"),
    NUMBER("number"),
    STRING("string"),
    BOOLEAN("boolean"),

    // Symbols
    TO("->"),
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
    AT("@"),

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