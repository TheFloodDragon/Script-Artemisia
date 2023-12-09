    package net.mugwort.mscript.ast.core

    sealed class Statement {
        data class Program(val body: List<Statement>) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "Program" to mutableMapOf(
                        "type" to "program",
                        "body" to body.map { it.toMap() }
                    )
                )
            }
        }
        data class EmptyStatement(val type: String = "EmptyStatement") : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "EmptyStatement" to mutableMapOf("type" to type)
                )
            }
        }
        data class BlockStatement(val body: List<Statement>) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "BlockStatement" to mutableMapOf(
                        "type" to "BlockStatement",
                        "body" to body.map { it.toMap() },
                    )
                )
            }
        }
        data class DoWhileStatement(val body: Statement, val rule: Expression) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "DoWhileStatement" to mutableMapOf(
                        "rule" to rule.toMap(),
                        "body" to body.toMap()
                    )
                )
            }
        }

        data class ExpressionStatement(val expression: Expression) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "ExpressionStatement" to mutableMapOf(
                        "type" to "ExpressionStatement",
                        "expression" to expression.toMap(),
                    )
                )
            }
        }
        data class VariableStatement(val declarations: List<VariableDeclaration>,val const : Boolean) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "VariableStatement" to mutableMapOf(
                            "type" to "VariableStatement",
                            "const" to const,
                            "declarations" to declarations.map { it.toMap() }
                        )

                )
            }
        }
        data class VariableDeclaration(val id: Expression.Identifier, val init: Expression?) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "VariableDeclaration" to mutableMapOf(
                        "type" to "VariableDeclaration",
                        "id" to id.toMap(),
                        "init" to init?.toMap()
                    )
                )
            }
        }
        data class VisitorStatement(val visitor: VisitorType,val state: Statement) : Statement() {
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "VisitorStatement" to mutableMapOf(
                        "type" to "VisitorStatement",
                        "visitor" to visitor,
                        "statement" to state.toMap(),
                    )
                )
            }
        }
        data class TryStatement(val body: BlockStatement,val exception : Expression,val catch : BlockStatement) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "TryStatement" to mutableMapOf(
                        "type" to "TryStatement",
                        "body" to body.toMap(),
                        "exception" to exception.toMap(),
                        "catch" to catch.toMap(),
                    )
                )
            }
        }

        data class IfStatement(val rule: Expression, val consequent: Statement, val alternate: Statement?) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "IfStatement" to mutableMapOf(
                        "type" to "IfStatement",
                        "rules" to rule.toMap(),
                        "body" to consequent.toMap(),
                        "else" to alternate?.toMap(),
                    )
                )
            }
        }
        data class WhileStatement(val rule: Expression, val body: Statement) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "WhileStatement" to mutableMapOf(
                        "type" to "WhileStatement",
                        "rule" to rule.toMap(),
                        "body" to body.toMap(),

                        )
                )
            }
        }
        data class ForStatement(val init: Expression, val rule: Expression, val body: Statement) : Statement(){
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "ForStatement" to mutableMapOf(
                        "type" to "ForStatement",
                        "init" to init.toMap(),
                        "range" to rule.toMap(),
                        "body" to body.toMap(),
                    )
                )
            }
        }
        data class ReturnStatement(val argument: Expression?) : Statement() {
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "ReturnStatement" to mutableMapOf(
                        "type" to "ReturnStatement",
                        "argument" to argument?.toMap(),
                    )
                )
            }
        }
        data class FunctionDeclaration(val identifier: Expression.Identifier, val params: List<VariableStatement>,val body: BlockStatement) : Statement() {
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "FunctionDeclaration" to mutableMapOf(
                        "type" to "FunctionDeclaration",
                        "identifier" to identifier.toMap(),
                        "params" to params.map { it.toMap() },
                        "body" to body.toMap()
                    )
                )
            }
        }
        data class ClassDeclaration(val identifier: Expression.Identifier,val params: List<VariableStatement>?, val body: BlockStatement) : Statement() {
            override fun toMap(): MutableMap<Any?, Any?> {
                return mutableMapOf(
                    "ClassDeclaration" to mutableMapOf(
                        "type" to "ClassDeclaration",
                        "identifier" to identifier.toMap(),
                        "params" to params?.map { it.toMap() },
                        "body" to body.toMap(),
                    )
                )
            }
        }

        enum class VisitorType{
            PUBLIC,
            PRIVATE
        }



        abstract fun toMap():MutableMap<Any?,Any?>

    }