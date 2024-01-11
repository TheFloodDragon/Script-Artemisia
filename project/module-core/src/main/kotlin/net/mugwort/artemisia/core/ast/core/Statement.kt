package net.mugwort.artemisia.core.ast.core

import net.mugwort.artemisia.core.ast.token.BigLocation

sealed class Statement {
    data class Program(val body: List<Statement>, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "Program" to mutableMapOf(
                    "type" to "program",
                    "location" to location.toMap(),
                    "body" to body.map { it.toMap() }
                )
            )
        }
    }

    data class EmptyStatement(val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "EmptyStatement" to mutableMapOf(
                    "type" to "EmptyStatement",
                    "location" to location.toMap(),
                )
            )
        }
    }

    data class BlockStatement(val body: List<Statement>, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "BlockStatement" to mutableMapOf(
                    "type" to "BlockStatement",
                    "location" to location.toMap(),
                    "body" to body.map { it.toMap() },
                )
            )
        }
    }

    data class DoWhileStatement(val body: Statement, val rule: Expression, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "DoWhileStatement" to mutableMapOf(
                    "rule" to rule.toMap(),
                    "location" to location.toMap(),
                    "body" to body.toMap()
                )
            )
        }
    }

    data class ExpressionStatement(val expression: Expression, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ExpressionStatement" to mutableMapOf(
                    "type" to "ExpressionStatement",
                    "location" to location.toMap(),
                    "expression" to expression.toMap(),
                )
            )
        }
    }

    data class VariableStatement(val declarations: VariableDeclaration, val const: Boolean, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "VariableStatement" to mutableMapOf(
                    "type" to "VariableStatement",
                    "location" to location.toMap(),
                    "const" to const,
                    "declarations" to declarations.toMap()
                )

            )
        }
    }

    data class VariableDeclaration(val id: Expression.Identifier, val init: Expression?) : Statement() {
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

    data class ImportStatement(val file: Expression, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "importStatement" to mutableMapOf(
                    "location" to location.toMap(),
                    "type" to "ImportStatement",
                    "Import" to file.toMap(),
                )
            )
        }
    }


    data class TryStatement(val body: BlockStatement, val exception: Expression? = null, val catch: BlockStatement? = null, val finally : BlockStatement? = null, val location: BigLocation) :
        Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "TryStatement" to mutableMapOf(
                    "type" to "TryStatement",
                    "location" to location.toMap(),
                    "body" to body.toMap(),
                    "exception" to exception?.toMap(),
                    "catch" to catch?.toMap(),
                    "finally" to finally?.toMap()
                )
            )
        }
    }

    data class IfStatement(val rule: Expression, val consequent: Statement, val alternate: Statement?, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "IfStatement" to mutableMapOf(
                    "type" to "IfStatement",
                    "location" to location.toMap(),
                    "rules" to rule.toMap(),
                    "body" to consequent.toMap(),
                    "else" to alternate?.toMap(),
                )
            )
        }
    }
    data class VisitorStatement(val type: VisitorType, val state: Statement?, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "VisitorStatement" to mutableMapOf(
                    "type" to "VisitorStatement",
                    "location" to location.toMap(),
                    "visitor" to type.id,
                    "statement" to state?.toMap(),
                )
            )
        }
    }
    data class EnumStatement(val id: Expression.Identifier, val enums : List<Expression>, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "EnumerationStatement" to mutableMapOf(
                    "type" to "EnumerationStatement",
                    "location" to location.toMap(),
                    "id" to id.toMap(),
                    "enum" to enums.map { it.toMap() },
                )
            )
        }
    }


    data class WhileStatement(val rule: Expression, val body: Statement, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "WhileStatement" to mutableMapOf(
                    "type" to "WhileStatement",
                    "location" to location.toMap(),
                    "rule" to rule.toMap(),
                    "body" to body.toMap(),

                    )
            )
        }
    }

    data class ForStatement(val init: Expression, val rule: Expression, val body: Statement, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ForStatement" to mutableMapOf(
                    "type" to "ForStatement",
                    "location" to location.toMap(),
                    "init" to init.toMap(),
                    "range" to rule.toMap(),
                    "body" to body.toMap(),
                )
            )
        }
    }

    data class ReturnStatement(val argument: Expression?, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ReturnStatement" to mutableMapOf(
                    "type" to "ReturnStatement",
                    "location" to location.toMap(),
                    "argument" to argument?.toMap(),
                )
            )
        }
    }

    data class EventStatement(val id : Expression.Identifier, val body: BlockStatement, val location: BigLocation): Statement(){
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ReturnStatement" to mutableMapOf(
                    "type" to "ReturnStatement",
                    "location" to location.toMap(),
                    "id" to id.toMap(),
                    "body" to body.toMap()
                )
            )
        }
    }

    data class FunctionDeclaration(
        val identifier: Expression.Identifier,
        val params: List<VariableStatement>,
        val returnValue : Expression,
        val body: BlockStatement,
        val location: BigLocation
    ) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "FunctionDeclaration" to mutableMapOf(
                    "type" to "FunctionDeclaration",
                    "location" to location.toMap(),
                    "identifier" to identifier.toMap(),
                    "returnValue" to returnValue.toMap(),
                    "params" to params.map { it.toMap() },
                    "body" to body.toMap()
                )
            )
        }
    }

    data class CaseDeclaration(val case: Expression, val body: Statement?, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "CaseDeclaration" to mutableMapOf(
                    "type" to "CaseDeclaration",
                    "location" to location.toMap(),
                    "case" to case.toMap(),
                    "body" to body?.toMap()
                )
            )
        }
    }

    data class SwitchStatement(val init: Expression, val body: List<CaseDeclaration>, val location: BigLocation) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "SwichStatement" to mutableMapOf(
                    "type" to "SwichStatement",
                    "location" to location.toMap(),
                    "init" to init.toMap(),
                    "cases" to body.map { it.toMap() }
                )
            )
        }
    }


    data class ClassDeclaration(
        val identifier: Expression.Identifier,
        val params: List<VariableStatement>?,
        val body: BlockStatement,
        val location: BigLocation
    ) : Statement() {
        override fun toMap(): MutableMap<Any?, Any?> {
            return mutableMapOf(
                "ClassDeclaration" to mutableMapOf(
                    "type" to "ClassDeclaration",
                    "location" to location.toMap(),
                    "identifier" to identifier.toMap(),
                    "params" to params?.map { it.toMap() },
                    "body" to body.toMap(),
                )
            )
        }
    }


    abstract fun toMap(): MutableMap<Any?, Any?>
    enum class VisitorType(val id: String){
        PUBLIC("public"),
        PRIVATE("private"),
        PROTECTED("protected"),
        ALREADY("already")
    }



}