package net.artemisia.script.common.token


enum class TokenType(val id: String) {
    // Keywords
    MODULE("module"),
    STRUCT("struct"),
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

    CATCH("catch"),
    FINALLY("finally"),
    IN("in"),
    BREAK("break"),
    CONTINUE("continue"),
    ELSE("else"),
    FOR("for"),
    IF("if"),
    IMPORT("import"),

    INTERFACE("interface"),
    ANNOTATION("annotation"),

    WHILE("while"),
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    OVERRIDE("override"),
    RETURN("return"),
    ENUM("enum"),


    TO("to"),
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

    UNTIL("->"),
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
    ARROW_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),

    ADDRESS("&"),
    DO("do"),
    SUPER("super"),
    THIS("this"),

    // EOF
    EOF("eof");





    companion object {

        val Binary = arrayListOf(
            PLUS,
            MINUS,
            MODULUS,
            SLASH,
            STAR,
            PLUS_EQUAL,
            MINUS_EQUAL,
            MODULUS_EQUAL,
            SLASH_EQUAL,
            STAR_EQUAL

        )
        val Unary = arrayListOf(
            Incrementing,
            BANG,
            Subtraction
        )

        val logical = arrayListOf(
            BANG_EQUAL,
            EQUAL_EQUAL,
            LESS,
            LESS_EQUAL,
            ARROW,
            ARROW_EQUAL
        )


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