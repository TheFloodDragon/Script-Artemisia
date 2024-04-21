package net.artemisia.script.compiler

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.compiler.runtime.compiler.core.code.CodePool
import net.artemisia.script.compiler.runtime.compiler.core.code.CodeType
import net.artemisia.script.compiler.runtime.compiler.core.data.DataPool
import net.artemisia.script.compiler.runtime.compiler.core.data.DataType
import net.artemisia.script.compiler.runtime.compiler.core.module.Module
import net.artemisia.script.compiler.runtime.compiler.core.module.ModuleCode
import net.artemisia.script.compiler.runtime.compiler.initialize.objects.other.*
import java.io.File

class Compiler(val file : File)  {
    private val program = Parser(file).parser()
    private val cache = "__artemisia-cache__"

    private val module = Module()
    private val main = CodePool()
    private val bytes = arrayListOf<Byte>()


    private var block = 0
    private var index = 0



    private val id = file.nameWithoutExtension
    private val filepath = file.parentFile.path + File.separator
    private var ext = ".apc"

    fun save(){
        compiler()
        val path = filepath  + cache + File.separator + id + ext
        val file = File(path)
        println("正在编译程序...")
        if (!file.parentFile.exists()){
            file.parentFile.mkdirs()
            file.writeBytes(bytes.toByteArray())
            file.createNewFile()
        }else{
            file.delete()
            file.writeBytes(bytes.toByteArray())
            file.createNewFile()
        }
        println("文件写入 -> $path")
        println("编译完成...")
    }


    fun compiler(ast : State = program ,code : CodePool = main): Int {
        when(val state = ast){

            is State.Module -> {
                val name = "main"

                for (i in state.body){
                    compiler(i)
                }

                module.codes.add(ModuleCode(index,name,main.getDatas().size,code))




                val bytes = ArrayList<Byte>()

                bytes.addAll(
                    arrayListOf(
                        0xAC.toByte(),
                        0xCB.toByte(),
                        0xBA.toByte(),
                        0xBA.toByte(),
                        0x00,
                        0x01,
                        0x00,
                        0x00
                    )
                )
                bytes.add(module.data.getDatas().size.toByte())
                for (i in module.data.getDatas()){
                    val index = i.key
                    bytes.add(index.toByte())
                    i.value.keys.forEach {
                        bytes.add(it.byte)
                        val v = i.value[it]
                        val value = when(it){
                            DataType.INT -> IntObject().gen(v.toString().toInt())
                            DataType.FLOAT -> FloatObject().gen(v.toString().toFloat())
                            DataType.DOUBLE -> DoubleObject().gen(v.toString().toDouble())
                            DataType.STRING -> StringObject().gen(v.toString())
                            DataType.IDENTIFIER -> StringObject().gen(v.toString())
                            DataType.PATH -> PathObject().gen(v.toString())
                        }
                        bytes.add(value.size.toByte())
                        bytes.addAll(value)
                    }
                }

                module.codes.forEach {
                    bytes.addAll(it.toBytes())
                }

/*                for (i in module.codes){
                    bytes.add(i.index.toByte())
                    bytes.add(i.id.length.toByte())
                    bytes.addAll(i.id.toByteArray().toList())
                    bytes.add(i.code.getDatas().size.toByte())
                    var indexC = 0
                    for (b in i.code.getDatas()){
                        bytes.add(indexC.toByte())
                        bytes.add(b.code.byte)
                        b.index?.toByte()?.let { bytes.add(it) }
                    }
                    indexC += 1
                }*/
                this.bytes.addAll(bytes)
            }


            is State.AnnotationState -> TODO()
            is State.BlockState -> {
                val name = "B$block"
                index += 1
                val pool = CodePool()
                for (i in state.body){
                    compiler(i,pool)
                }
                val value = ModuleCode(index,name,pool.getDatas().size,pool)
                module.codes.add(value)
                code.push(CodeType.BLOCK,module.codes.indexOf(value))
                block += 1
            }
            is State.DoWhileState -> TODO()
            is State.EmptyState -> {}
            is State.EnumState -> TODO()
            is State.ExpressionState -> {
                return expr(state.expr, code = code)
            }
            is State.ForState -> TODO()
            is State.IfState -> TODO()
            is State.ImportState -> TODO()
            is State.MethodDeclaration -> TODO()

            is State.ReturnState -> TODO()
            is State.SwitchState -> TODO()
            is State.TryState -> TODO()
            is State.VariableDeclaration -> TODO()
            is State.VisitorState -> TODO()
            is State.WhileState -> TODO()
            is State.CaseDeclaration -> TODO()
            is State.ClassDeclaration -> TODO()
            is State.AnnotationDeclaration -> TODO()
        }
        return -1
    }

    private fun expr(y1: Expr,pool : DataPool = module.data,code : CodePool = main): Int {
        when(val expr = y1){
            is Expr.AddressableExpr -> TODO()
            is Expr.AssignmentExpr -> {
                if (expr.left is State.ExpressionState){
                   if (isLiteral((expr.left as State.ExpressionState).expr)) code.push(CodeType.PUSH,compiler(expr.left)) else compiler(expr.left)
                }else {
                    compiler(expr.left)
                }
                if (expr.right is State.ExpressionState){
                    if (isLiteral((expr.right as State.ExpressionState).expr)) code.push(CodeType.PUSH,compiler(expr.right)) else compiler(expr.right)
                }else {
                    compiler(expr.right)
                }
                return code.indexOf(code.push(CodeType.AGT))

            }
            is Expr.BinaryExpr -> TODO()
            is Expr.BooleanLiteral -> TODO()
            is Expr.CallExpr -> {
                for (i in expr.arguments){
                    when(i){
                        is State.ExpressionState -> {
                            if (isLiteral(i.expr)){
                                code.push(CodeType.PUSH,compiler(i,code))
                            }else compiler(i,code)
                        }

                        else -> compiler(i,code)
                    }

                }
                val calls = expr(expr.caller,pool, code)
                val call = code.push(CodeType.CALL,calls)
                return code.indexOf(call)
            }
            is Expr.GenericExpr -> TODO()
            is Expr.GroupExpr -> TODO()
            is Expr.Identifier -> {
                return pool.push(DataType.IDENTIFIER,expr.name)
            }
            is Expr.InExpr -> TODO()
            is Expr.Lambda -> TODO()
            is Expr.LogicalExpr -> {
                if (expr.right is Expr.LogicalExpr){
                    expr(expr.right,pool, code)
                }
                if (expr.left is Expr.LogicalExpr){
                    expr(expr.left,pool, code)
                }
                val op = pool.push(DataType.STRING,expr.operator)
                if (expr.right !is Expr.LogicalExpr) code.push(CodeType.PUSH,expr(expr.right,pool, code))
                if (expr.left !is Expr.LogicalExpr) code.push(CodeType.PUSH,expr(expr.left,pool, code))
                code.push(CodeType.LOADIFOP,op)
                code.push(CodeType.CMP)
                return 0

            }
            is Expr.MemberExpr -> TODO()
            Expr.NullLiteral -> TODO()
            is Expr.NumericLiteral -> {
                return when(expr.value){
                    is Int -> {
                        pool.push(DataType.INT,expr.value.toInt())
                    }
                    is Double -> {
                        pool.push(DataType.DOUBLE,expr.value.toDouble())
                    }
                    else -> {
                        pool.push(DataType.FLOAT,expr.value.toFloat())
                    }

                }
            }
            is Expr.PointerExpr -> TODO()
            is Expr.StringLiteral -> TODO()
            is Expr.ToExpr -> TODO()
            is Expr.UnaryExpr -> TODO()
            is Expr.UntilExpr -> TODO()
        }
    }
    fun isLiteral(expr: Expr): Boolean {
        return when (expr){
            is Expr.BooleanLiteral, is Expr.NumericLiteral,is Expr.StringLiteral,is Expr.Identifier,is Expr.NullLiteral -> true
            else -> false
        }
    }

}