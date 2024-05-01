package compiler

import common.ast.Expr
import common.ast.State
import compiler.runtime.compiler.core.code.CodePool
import compiler.runtime.compiler.core.code.CodeType
import compiler.runtime.compiler.core.data.DataPool
import compiler.runtime.compiler.core.data.DataType
import compiler.runtime.compiler.core.module.Module
import compiler.runtime.compiler.core.module.ModuleCode
import compiler.runtime.compiler.initialize.objects.other.*
import java.io.File

class Compiler(val file : File)  {
    private val program = Parser(file).parser()
    private val cache = "__artemisia-cache__"

    private val module = Module()
    private val main = CodePool()
    private val method = CodePool()
    private val bytes = arrayListOf<Byte>()


    private var block = 0
    private var index = 0



    private val id = file.nameWithoutExtension
    private val filepath = file.parentFile.path + File.separator
    private var mext = ".module"
    private var cext = ".class"

    private val path = filepath  + cache + File.separator + id + mext
    val byteCodeFile : File = File(path)

    fun save(): Compiler {
        compiler()

        if (!byteCodeFile.parentFile.exists()){
            byteCodeFile.parentFile.mkdirs()
            byteCodeFile.writeBytes(bytes.toByteArray())
            byteCodeFile.createNewFile()
        }else{
            byteCodeFile.delete()
            byteCodeFile.writeBytes(bytes.toByteArray())
            byteCodeFile.createNewFile()
        }

        return this
    }


    fun compiler(ast : State = program ,code : CodePool = main,shouldPush : Boolean = false): Int {
        when(val state = ast){

            is State.Module -> {
                val name = state.id.name

                for (i in state.body){
                    if (i !is State.EmptyState){
                        compiler(i)
                        main.push(CodeType.CLS)
                    }
                }

                module.codes.add(ModuleCode(index,name,ModuleCode.BlockType.MODULE,main.getDatas().size,main))
                module.codes.add(ModuleCode(index,"methods",ModuleCode.BlockType.NORMAL,method.getDatas().size,method))



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
                //0x0A : Module
                //0x0B : Class
                bytes.add(0x0A)

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
                bytes.add(module.codes.size.toByte())
                module.codes.forEach {
                    bytes.addAll(it.toBytes())
                }

                this.bytes.addAll(bytes)
            }


            is State.AnnotationState -> TODO()
            is State.BlockState -> {
                block(state, code)
            }
            is State.DoWhileState -> TODO()
            is State.EmptyState -> {}
            is State.EnumState -> TODO()
            is State.ExpressionState -> {
                val i = expr(state.expr, code = code)
                if (isLiteral(state.expr) && shouldPush){
                    code.push(CodeType.PUSH,i)
                }
                return i
            }
            is State.ForState -> TODO()
            is State.IfState -> TODO()
            is State.ImportState -> TODO()
            is State.MethodDeclaration -> {
                method.push(CodeType.LOADDATA,expr(state.identifier,module.data,method))
                for (i in state.params){
                    method.push(CodeType.SETPARAMS,expr(i.id,module.data,method))
                    method.push(CodeType.INVKOETYPE,expr(i.type ?: Expr.Identifier("Auto"),module.data,method))
                    if (i.init != null){
                        compiler(i.init!!,method,shouldPush = true)
                    }
                    if (i.const){
                        method.push(CodeType.SAVEPARAMSCNT)
                    }else{
                        method.push(CodeType.SAVEPARAMS)
                    }

                }
                method.push(CodeType.INVKOETYPE,expr(state.type ?: Expr.Identifier("Auto"),module.data,method))
                if (state.body != null) block(state.body!!,method,"M",true)
                return 0
            }

            is State.ReturnState -> {
                if (state.argument != null){
                    when(val i = state.argument!!){
                        is State.ExpressionState -> {
                            if (isLiteral(i.expr)){
                                code.push(CodeType.PUSH,expr(i.expr, code = code))
                            }else{
                                compiler(state.argument!!,code = code,shouldPush = true)
                            }
                        }
                        else -> compiler(state.argument!!,code = code,shouldPush = true)
                    }
                }
                code.push(CodeType.RET)
            }
            is State.SwitchState -> TODO()
            is State.TryState -> TODO()
            is State.VariableDeclaration -> TODO()
            is State.VisitorState -> TODO()
            is State.WhileState -> TODO()
            is State.CaseDeclaration -> TODO()
            is State.ClassDeclaration -> TODO()
            is State.AnnotationDeclaration -> TODO()
            is State.InterfaceDeclaration -> TODO()
            is State.StructDeclaration -> TODO()
        }
        return -1
    }

    private fun block(state: State.BlockState,code: CodePool,id : String = "B",method : Boolean = false){
        val name = "$id$block"
        index += 1
        val pool = CodePool()
        for (i in state.body){
            compiler(i,pool,shouldPush = true)
        }
        val value = if (method){
            ModuleCode(index,name,ModuleCode.BlockType.METHOD,pool.getDatas().size,pool)
        }else ModuleCode(index,name,ModuleCode.BlockType.NORMAL,pool.getDatas().size,pool)

        module.codes.add(value)
        if (method){
            code.push(CodeType.NEWMETHOD,module.codes.indexOf(value))
        } else code.push(CodeType.BLOCK,module.codes.indexOf(value))

        block += 1
    }



    private fun expr(y1: Expr,pool : DataPool = module.data,code : CodePool = main): Int {
        when(val expr = y1){
            is Expr.AddressableExpr -> TODO()
            is Expr.AssignmentExpr -> {
                if (expr.left is State.ExpressionState){
                   if (isLiteral((expr.left as State.ExpressionState).expr)) code.push(CodeType.PUSH,compiler(expr.left,shouldPush = true)) else compiler(expr.left,shouldPush = true)
                }else {
                    compiler(expr.left,shouldPush = true)
                }
                if (expr.right is State.ExpressionState){
                    if (isLiteral((expr.right as State.ExpressionState).expr)) code.push(CodeType.PUSH,compiler(expr.right,shouldPush = true)) else compiler(expr.right,shouldPush = true)
                }else {
                    compiler(expr.right,shouldPush = true)
                }
                return code.indexOf(code.push(CodeType.AGT))

            }
            is Expr.BinaryExpr -> {
                if (expr.left is Expr.StringLiteral || expr.left is Expr.NumericLiteral) {
                    code.push(CodeType.PUSH,expr(expr.left, pool, code))
                } else {
                    expr(expr.left, pool, code)
                }

                if (expr.right is Expr.StringLiteral || expr.right is Expr.NumericLiteral) {
                    code.push(CodeType.PUSH,expr(expr.right, pool, code))
                } else {
                    expr(expr.right, pool, code)
                }

                return code.indexOf(when(expr.operator){
                    "+" -> code.push(CodeType.ADD)
                    "*" -> code.push(CodeType.MIT)
                    "-" -> code.push(CodeType.MIN)
                    "/" -> code.push(CodeType.DIV)
                    else -> code.push(CodeType.MOD)
                })
            }
            is Expr.BooleanLiteral -> TODO()
            is Expr.CallExpr -> {
                for (i in expr.arguments){
                    compiler(i,code,shouldPush = true)
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
            is Expr.MemberExpr -> {
                when (expr.left) {
                    is Expr.Identifier -> {
                        val i = pool.push(DataType.IDENTIFIER, (expr.left as Expr.Identifier).name)
                        code.push(CodeType.LOADATTRIBUTE,i)
                    }

                    is Expr.CallExpr -> {
                        for (i in (expr.left as Expr.CallExpr).arguments){
                            when(i){
                                is State.ExpressionState -> {
                                    compiler(i,code,shouldPush = true)
                                }
                                else -> compiler(i,code,shouldPush = true)
                            }

                        }
                        val calls = expr((expr.left as Expr.CallExpr).caller,pool, code)
                        code.push(CodeType.CALLARRTIBUTE,calls)
                    }

                    else -> expr(expr.left,pool, code)
                }
                when (expr.right) {
                    is Expr.Identifier -> {
                        val i = pool.push(DataType.IDENTIFIER, (expr.right as Expr.Identifier).name)
                        code.push(CodeType.LOADATTRIBUTE,i)
                    }

                    is Expr.CallExpr -> {
                        for (i in (expr.right as Expr.CallExpr).arguments){
                            when(i){
                                is State.ExpressionState -> {
                                    compiler(i,code,shouldPush = true)
                                }

                                else -> compiler(i,code,shouldPush = true)
                            }

                        }
                        val calls = expr((expr.right as Expr.CallExpr).caller,pool, code)
                         code.push(CodeType.CALLARRTIBUTE,calls)
                    }

                    else -> expr(expr.right,pool, code)
                }
                return 0
            }
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
            is Expr.StringLiteral -> {
                return pool.push(DataType.STRING,expr.value)
            }
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