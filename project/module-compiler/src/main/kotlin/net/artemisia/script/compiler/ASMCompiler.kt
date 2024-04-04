package net.artemisia.script.compiler

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.compiler.runtime.compiler.Global.data
import net.artemisia.script.compiler.runtime.compiler.aura.CodeAura
import net.artemisia.script.compiler.runtime.compiler.aura.ProgramAura
import net.artemisia.script.compiler.runtime.compiler.builder.ASMethodBuilder
import net.artemisia.script.compiler.runtime.compiler.helper.ASMCodeHelper
import net.artemisia.script.compiler.runtime.compiler.types.ASMCode
import net.artemisia.script.compiler.runtime.compiler.types.DataType
import java.io.File

class ASMCompiler(file : File) {
    private val ast = Parser(file).parser()
    private val main = CodeAura()
    private val methods = ArrayList<ASMethodBuilder>()
    val program = ProgramAura(file,ast.id.name, data,main,methods)

//    init {
//        data.add(DataType.DT,"Int")
//        data.add(DataType.DT,"String")
//        data.add(DataType.DT,"Double")
//        data.add(DataType.DT, "Float")
//        data.add(DataType.DT,"Number")
//        data.add(DataType.DT,"Auto")
//    }
    fun compiler(): ProgramAura {
        for (i in ast.body){
            getState(i,main)
        }
        main.add(ASMCode.END, arrayListOf(),"执行结束")
        return program
    }

    fun getState(i : State,aura: CodeAura,isVar : Boolean = false){
        when(val state = i){
            is State.BlockState -> {
                for (iv in state.body){
                    getState(iv,aura)
                }
            }
            is State.CaseDeclaration -> TODO()
            is State.ClassDeclaration -> TODO()
            is State.DoWhileState -> TODO()
            is State.EmptyState -> {}

            is State.EnumState -> TODO()
            is State.EventState -> TODO()
            is State.ExpressionState -> {
                when(state.expr){
                    is Expr.Identifier,is Expr.StringLiteral,is Expr.NumericLiteral,is Expr.NullLiteral,is Expr.BooleanLiteral,is Expr.VoidLiteral -> {
                        val iz = getExpr(state.expr)
                        if (isVar && state.expr is Expr.Identifier){
                            aura.add(
                                ASMCode.PUSH,
                                arrayListOf(
                                    ASMCodeHelper(ASMCodeHelper.ValueType.POINTER, (state.expr as Expr.Identifier).name),
                                ),
                                "压入量 ${search(iz)}"


                            )
                        }else{
                            aura.add(
                                ASMCode.PUSH,
                                arrayListOf(
                                    ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,iz),
                                ),
                                "压入参数 ${search(iz)}"


                            )

                        }


                    }

                    else -> {
                        getExpr(state.expr)

                    }
                }


            }
            is State.ForState -> TODO()
            is State.IfState -> TODO()
            is State.ImportState -> TODO()
            is State.MethodDeclaration -> {
                val codes = CodeAura()
                val params = ArrayList<ASMCodeHelper>()
                val id = getExpr(state.identifier)
                val type = getExpr(state.type ?: Expr.Identifier("Void"))

                for (iz in state.params){
                    val m = getExpr(iz.id)
                    val typez = getExpr(iz.type ?: Expr.Identifier("Auto"))
                    params.add(ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,m))
                    codes.add(
                        ASMCode.SP,
                        arrayListOf(
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id),
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,typez)
                        ),
                        "设定方法参数 ${iz.id.name} 类型为 ${search(typez)}"
                    )
                    if (iz.init != null){
                        getState(iz.init!!,codes)
                    }
                    codes.add(ASMCode.MOVL, arrayListOf(ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id)),"将栈顶值移入 ${iz.id.name}" )
                }
                if (state.body != null) getState(state.body!!,codes)
                val im = ASMethodBuilder(ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id), ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,type),params, codes)
                methods.add(im)
                aura.add(ASMCode.IM, arrayListOf(ASMCodeHelper(ASMCodeHelper.ValueType.POINTER,im.id)),"新建方法")


            }
            is State.Module -> TODO()
            is State.ReturnState -> TODO()
            is State.SwitchState -> TODO()
            is State.TryState -> TODO()
            is State.VariableDeclaration -> {
                if (state.const){
                    val type = getExpr(state.type ?: Expr.Identifier("Auto"))
                    val id = getExpr(state.id)
                    aura.add(
                        ASMCode.IL,
                        arrayListOf(
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id),
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,type)
                        ),
                        "设定常量 ${state.id.name} 类型为 ${search(type)}"
                    )
                    if (state.init != null){
                        getState(state.init!!,aura,true)
                    }
                    aura.add(ASMCode.MOVL, arrayListOf(ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id)),"将栈顶值移入 ${state.id.name}" )
                }else{
                    val type = getExpr(state.type ?: Expr.Identifier("Auto"))
                    val id = getExpr(state.id)
                    aura.add(
                        ASMCode.IV,
                        arrayListOf(
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id),
                            ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,type)
                        ),
                        "设定变量 ${state.id.name} 类型为 ${search(type)}"
                    )
                    if (state.init != null){
                        getState(state.init!!,aura,true)
                    }
                    aura.add(ASMCode.MOVL, arrayListOf(
                        ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id
                        )
                    ),"将栈顶值移入 ${state.id.name}")
                }
            }
            is State.VisitorState -> TODO()
            is State.WhileState -> TODO()
            else -> {}
        }
    }
    fun getExpr(i: Expr,aura: CodeAura = main): Int {
        when(val expr = i){
            is Expr.InExpr -> TODO()
            is Expr.AssignmentExpr -> TODO()
            is Expr.BinaryExpr -> TODO()
            is Expr.BooleanLiteral -> TODO()
            is Expr.CallExpr -> {
                val id = getExpr(expr.caller)
                for (c in expr.arguments){
                    getState(c,aura)
                }
                aura.add(ASMCode.CALL, arrayListOf(ASMCodeHelper(ASMCodeHelper.ValueType.LOADCONSTANT,id)),"加载方法 ${search(id)} 并压入栈")
                return 0
            }
            is Expr.GenericExpr -> TODO()
            is Expr.GroupExpr -> TODO()
            is Expr.Identifier -> {
                return data.add(DataType.DT,expr.name)
            }
            is Expr.Lambda -> TODO()
            is Expr.LogicalExpr -> TODO()
            is Expr.MemberExpr -> TODO()
            Expr.NullLiteral -> TODO()
            is Expr.NumericLiteral -> {
                return if (!expr.value.toString().toFloat().isFinite()){
                    when(expr.value){
                        is Float -> data.add(DataType.DF,expr.value.toFloat())
                        is Double -> data.add(DataType.DD,expr.value.toDouble())
                        else -> { -1 }
                    }
                }else{
                    data.add(DataType.DI,expr.value.toInt())
                }
            }
            is Expr.StringLiteral -> TODO()
            is Expr.ToExpr -> TODO()
            is Expr.UntilExpr -> TODO()
            is Expr.UnaryExpr -> TODO()
            Expr.VoidLiteral -> TODO()
            else -> TODO()
        }
    }
    fun show(){
        program.show()
    }
    fun search(index : Int): Any {
        return data.getData()[index].value
    }

}