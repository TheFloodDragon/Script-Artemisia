package common.ast

import common.location.BigLocation


sealed class State {
    /**/
    data class Module(val id : Expr.Identifier,val body: ArrayList<State>, val location: BigLocation) : State()
    /**/
    data class AnnotationState(val id : Expr.Identifier, val calls : List<Expr>?, val location: BigLocation) : State()
    /**/
    data class EmptyState(val location: BigLocation) : State()
    /**/
    data class BlockState(val body: List<State>, val location: BigLocation) : State()

    data class DoWhileState(val body: State, val rule: Expr, val location: BigLocation) : State()


    /**/
    data class ExpressionState(val expr: Expr, val location: BigLocation) : State()
    /**/
    data class VariableDeclaration(val id: Expr.Identifier, val init: State?, val type: Expr?,val const: Boolean, val location: BigLocation) : State()
    /**/
    data class ImportState(val file: Expr,val obj : ArrayList<Expr>,val all : Boolean = false, val location: BigLocation) : State()

    data class TryState(val body: BlockState, val exception: Expr? = null, val catch: BlockState? = null, val finally: BlockState? = null, val location: BigLocation) : State()
    /**/
    data class IfState(val rule: Expr, val consequent: State, val alternate: State?, val location: BigLocation) : State()
    /**/
    data class VisitorState(val type: String, val state: State?, val location: BigLocation) : State()

    data class EnumState(val id: Expr.Identifier, val enums: List<Expr>, val location: BigLocation) : State()
    /**/
    data class WhileState(val rule: Expr, val body: State, val location: BigLocation) : State()
    /**/
    data class ForState(val rule: Expr, val body: State, val location: BigLocation) : State()
    /**/
    data class ReturnState(val argument: State?, val location: BigLocation) : State()
    /**/
    data class MethodDeclaration(val identifier: Expr, val params: List<VariableDeclaration>, val body: BlockState?, val type: Expr?, val location: BigLocation) : State()


    data class CaseDeclaration(val case: Expr, val body: State?, val location: BigLocation) : State()
    data class SwitchState(val init: Expr, val body: List<CaseDeclaration>, val location: BigLocation) : State()


    data class ClassDeclaration(val identifier: Expr.Identifier, val params: List<State>, val inherit: List<Expr>, val body: BlockState?, val location: BigLocation) : State()
    data class AnnotationDeclaration(val identifier : Expr.Identifier,val params: List<State>?, val body: BlockState?, val location: BigLocation) : State()
    data class StructDeclaration(val identifier: Expr.Identifier, val params: List<State>, val inherit: List<Expr>, val body: BlockState?, val location: BigLocation) : State()
    data class InterfaceDeclaration(val  id: Expr.Identifier,val params: List<State>, val body: BlockState?, val location: BigLocation) : State()

}