package net.artemisia.script.compiler

import net.artemisia.script.common.ast.Expr
import net.artemisia.script.common.ast.State
import net.artemisia.script.common.expection.thrower
import net.artemisia.script.common.vm.ByteCode
import net.artemisia.script.compiler.runtime.compiler.ConstantPool
import net.artemisia.script.compiler.runtime.compiler.objects.module.*
import net.artemisia.script.compiler.runtime.compiler.objects.other.BooleanObject
import net.artemisia.script.compiler.runtime.compiler.objects.other.IdentifierObject
import net.artemisia.script.compiler.runtime.compiler.objects.other.NumberObject
import net.artemisia.script.compiler.runtime.compiler.objects.other.StringObject
import java.io.File
import java.time.LocalDateTime


class Compiler(private val file: File) {
    private val body = Parser(file).parser().body

    private var lastCommand: ByteCode = ByteCode.SaveVariable(0x00)
    private var stacksize: Int = 1
    private val constants = ConstantPool()
    private val codes: ArrayList<CodeObject> = arrayListOf()

    private val funcs: ArrayList<FunctionObject> = arrayListOf()
    private val listeners: ArrayList<EventObject> = arrayListOf()
    private val visits: ArrayList<VisitorObject> = arrayListOf()


    private val currentDateTime = LocalDateTime.now()

    fun save() {
        for (i in body) {
            codes.addAll(statement(i, constants))
        }
        val compiled = File(file.parentFile.path + "/" + file.nameWithoutExtension + ".apc")
        val obj = ModuleObject(
            byteArrayOf(0x00, 0x00, 3.toByte(), 0x00),
            stacksize,
            constants,
            codes,
            funcs,
            listeners,
            visits,
            file.absolutePath.toString().toByteArray(),
            byteArrayOf(
                0x01,
                currentDateTime.year.toByte(),
                0x02,
                currentDateTime.monthValue.toByte(),
                0x03,
                currentDateTime.dayOfMonth.toByte(),
                0x04,
                currentDateTime.hour.toByte(),
                0x05,
                currentDateTime.minute.toByte()
            )
        )
        compiled.writeBytes(obj.toByte())
        compiled.createNewFile()
    }

    private fun statement(s: State, pool: ConstantPool, autoVisitor: Boolean = true): ArrayList<CodeObject> {

        return when (s) {
            is State.BlockState -> {
                val array: ArrayList<CodeObject> = arrayListOf()
                for (i in s.body) {
                    array.addAll(statement(i, pool))
                }
                array
            }

            is State.CaseDeclaration -> TODO()
            is State.ClassDeclaration -> TODO()
            is State.DoWhileState -> TODO()
            is State.EmptyState -> TODO()
            is State.EnumState -> TODO()
            is State.EventState -> {
                val args: ArrayList<ArgObject> = arrayListOf()
                val array: ArrayList<CodeObject> = arrayListOf()
                val id = TypeGetter(s.id)
                for (i in s.params) {
                    val type = TypeGetter(i.type!!)
                    args.add(
                        ArgObject(
                            IdentifierObject(i.id.name.length, i.id.name),
                            type
                        )
                    )
                }

                val codes = statement(s.body, pool)
                val event = EventObject((id.obj as IdentifierObject), args, codes)
                listeners.add(event)
                array.add(toCode(ByteCode.EventListener(listeners.indexOf(event).toByte())))
                array
            }

            is State.ExpressionState -> {
                val array: ArrayList<CodeObject> = arrayListOf()
                if (s.expr is Expr.CallExpr) {
                    val caller = expression(s.expr, pool)
                    for (expr in (s.expr as Expr.CallExpr).arguments) {
                        array.addAll(statement(expr, pool))
                    }
                    val index = pool.search(caller.toList())
                    array.add(toCode(ByteCode.Call(index!!.toByte())))
                } else {
                    val index = pool.search(expression(s.expr, pool).toList())
                    array.add(toCode(ByteCode.Push(index!!.toByte())))
                }
                array
            }

            is State.ForState -> TODO()
            is State.MethodDeclaration -> {
                val args: ArrayList<ArgObject> = arrayListOf()
                val array: ArrayList<CodeObject> = arrayListOf()
                val id = TypeGetter(s.identifier)
                for (i in s.params) {
                    val type = TypeGetter(i.type!!)
                    args.add(
                        ArgObject(
                            IdentifierObject(i.id.name.length, i.id.name),
                            type
                        )
                    )
                }
                val codes = statement(s.body!!, pool)
                val returnType = TypeGetter(s.returnValue)
                val func = FunctionObject((id.obj as IdentifierObject), args, codes, returnType)
                funcs.add(func)
                array.add(toCode(ByteCode.CreateFunction(funcs.indexOf(func).toByte())))
                if (autoVisitor) {
                    visits.add(
                        VisitorObject(
                            VisitorObject.VisitorType.PUBLIC,
                            VisitorObject.VisitObject.FUNCTION, funcs.indexOf(func)
                        )
                    )
                }
                array
            }

            is State.IfState -> {
                TODO()
            }

            is State.ImportState -> {
                TODO()
            }

            is State.Module -> TODO()
            is State.ReturnState -> TODO()
            is State.SwitchState -> TODO()
            is State.TryState -> TODO()
            is State.VariableDeclaration -> {
                val id = expression(s.id, pool)
                val array: ArrayList<CodeObject> = arrayListOf()
                if (s.init != null && s.const) {


                    val iIndex = pool.search(id.toList())
                    array.addAll(statement(s.init!!, pool))
                    typeInvoke(s.type, pool)?.let { toCode(it) }?.let { array.add(it) }
                    array.add(toCode(ByteCode.SaveConstant(iIndex!!.toByte())))
                    if (autoVisitor) {
                        visits.add(
                            VisitorObject(
                                VisitorObject.VisitorType.PUBLIC,
                                VisitorObject.VisitObject.VARIABLE, iIndex
                            )
                        )
                    }
                    return array
                } else if (!s.const && s.init == null) {
                    val iIndex = pool.search(id.toList())
                    typeInvoke(s.type, pool)?.let { toCode(it) }?.let { array.add(it) }
                    array.add(toCode(ByteCode.SetVariable(iIndex!!.toByte())))
                    if (autoVisitor) {
                        visits.add(
                            VisitorObject(
                                VisitorObject.VisitorType.PUBLIC,
                                VisitorObject.VisitObject.VARIABLE, iIndex
                            )
                        )
                    }
                    return array
                } else if (!s.const && s.init != null) {
                    val iIndex = pool.search(id.toList())
                    array.addAll(statement(s.init!!, pool))
                    typeInvoke(s.type, pool)?.let { toCode(it) }?.let { array.add(it) }
                    array.add(toCode(ByteCode.SaveVariable(iIndex!!.toByte())))
                    return array
                } else {
                    thrower.send("Constant Must Init", "notInit", file, s.location, true)
                    return array
                }
            }

            is State.WhileState -> TODO()
            else -> arrayListOf()
        }
    }

    private fun expression(e: Expr, pool: ConstantPool): ByteArray {
        return when (e) {
            is Expr.AssignmentExpr -> TODO()
            is Expr.BinaryExpr -> {
                TODO()
            }

            is Expr.BooleanLiteral -> TODO()
            is Expr.CallExpr -> {
                val array = ArrayList<Byte>()
                val caller = expression(e.caller, pool)
                array.addAll(caller.toList())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.IDENTIFIER.id, caller)
                }
                array.toByteArray()
            }

            is Expr.GroupExpr -> TODO()
            is Expr.Identifier -> {
                val ret = IdentifierObject(e.name.length, e.name)
                val array = arrayListOf(TypeObject.Type.IDENTIFIER.id)
                array.addAll(ret.toByte().toList())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.IDENTIFIER.id, ret.id.toByteArray())
                }
                array.toByteArray()
            }

            is Expr.LogicalExpr -> TODO()
            is Expr.MemberExpr -> TODO()
            Expr.NullLiteral -> TODO()
            is Expr.NumericLiteral -> {
                val array = ArrayList<Byte>()
                if (e.value == null) {
                    return array.toByteArray()
                }
                val ret = NumberObject(e.value!!)


                array.addAll(ret.toByte().toList())
                array.add(0, TypeObject.Type.NUMBER.id)
                array.add(1, ret.getLen())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.NUMBER.id, ret.toByte())
                }
                array.toByteArray()
            }

            is Expr.ObjectLiteral -> TODO()
            is Expr.StringLiteral -> {
                val array = ArrayList<Byte>()
                if (e.value == null) {
                    return array.toByteArray()
                }

                val ret = StringObject(e.value!!)

                array.addAll(ret.toByte().toList())
                array.add(0, TypeObject.Type.STRING.id)
                array.add(1, ret.toByte().size.toByte())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.STRING.id, ret.toByte())
                }
                array.toByteArray()
            }

            is Expr.UnaryExpr -> TODO()
            Expr.VoidLiteral -> TODO()
            is Expr.Constructor -> TODO()
            is Expr.ToExpr -> TODO()
            is Expr.Lambda -> TODO()
            is Expr.Parameter -> TODO()
            is Expr.GenericExpr -> TODO()
        }
    }

    fun variableType(i: Expr, pool: ConstantPool): Byte {
        return when (val init = i) {
            is Expr.StringLiteral -> {
                0x01
            }

            is Expr.NumericLiteral -> {
                0x02
            }

            is Expr.BooleanLiteral -> {
                0x03
            }

            is Expr.Identifier -> {
                val value = expression(i, pool)
                constants.search(value.toList())!!.toByte()
            }

            else -> {
                0x00
            }
        }
    }

    fun TypeGetter(i: Expr): TypeObject {
        return when (val init = i) {
            is Expr.BooleanLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.BOOLEAN, BooleanObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.BOOLEAN)
                }
            }

            is Expr.StringLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.STRING, StringObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.STRING)
                }
            }

            is Expr.Identifier -> {

                TypeObject(TypeObject.Type.IDENTIFIER, IdentifierObject(init.name.length, init.name))
            }

            is Expr.NumericLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.NUMBER, NumberObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.NUMBER)
                }
            }

            else -> {
                TypeObject(TypeObject.Type.VOID)
            }
        }
    }

    fun toCode(command: ByteCode): CodeObject {
        if (command is ByteCode.Push && lastCommand is ByteCode.Push) stacksize += 1
        lastCommand = command
        return CodeObject(command)
    }

    fun typeInvoke(expr: Expr?, pool: ConstantPool): ByteCode? {
        if (expr != null) {
            val type = variableType(expr, pool)
            return ByteCode.InvokeType(type)
        }
        return null
    }
}

