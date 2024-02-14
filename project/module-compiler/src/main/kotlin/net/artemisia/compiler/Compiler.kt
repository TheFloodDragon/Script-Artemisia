package net.artemisia.compiler

import net.artemisia.api.expection.thrower
import net.artemisia.core.asm.ByteCode
import net.artemisia.core.ast.core.Expression
import net.artemisia.core.ast.core.Statement
import net.artemisia.runtime.compiler.ConstantPool
import net.artemisia.runtime.compiler.objects.module.*
import net.artemisia.runtime.compiler.objects.other.BooleanObject
import net.artemisia.runtime.compiler.objects.other.IdentifierObject
import net.artemisia.runtime.compiler.objects.other.NumberObject
import net.artemisia.runtime.compiler.objects.other.StringObject
import java.io.File
import java.time.LocalDateTime


class Compiler(private val file: File) {
    private val body = Parser(file.readText(), file).parser().body

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

    private fun statement(s: Statement, pool: ConstantPool, autoVisitor: Boolean = true): ArrayList<CodeObject> {

        return when (s) {
            is Statement.BlockStatement -> {
                val array: ArrayList<CodeObject> = arrayListOf()
                for (i in s.body) {
                    array.addAll(statement(i, pool))
                }
                array
            }

            is Statement.CaseDeclaration -> TODO()
            is Statement.ClassDeclaration -> TODO()
            is Statement.DoWhileStatement -> TODO()
            is Statement.EmptyStatement -> TODO()
            is Statement.EnumStatement -> TODO()
            is Statement.EventStatement -> {
                val args: ArrayList<ArgObject> = arrayListOf()
                val array: ArrayList<CodeObject> = arrayListOf()
                val id = TypeGetter(s.id)
                for (i in s.params) {
                    val type = TypeGetter(i.declarations.type!!)
                    args.add(
                        ArgObject(
                            IdentifierObject(i.declarations.id.name.length, i.declarations.id.name),
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

            is Statement.ExpressionStatement -> {
                val array: ArrayList<CodeObject> = arrayListOf()
                if (s.expression is Expression.CallExpression) {
                    val caller = expression(s.expression, pool)
                    for (expr in (s.expression as Expression.CallExpression).arguments) {
                        array.addAll(statement(Statement.ExpressionStatement(expr, s.location), pool))
                    }
                    val index = pool.search(caller.toList())
                    array.add(toCode(ByteCode.Call(index!!.toByte())))
                } else {
                    val index = pool.search(expression(s.expression, pool).toList())
                    array.add(toCode(ByteCode.Push(index!!.toByte())))
                }
                array
            }

            is Statement.ForStatement -> TODO()
            is Statement.FunctionDeclaration -> {
                val args: ArrayList<ArgObject> = arrayListOf()
                val array: ArrayList<CodeObject> = arrayListOf()
                val id = TypeGetter(s.identifier)
                for (i in s.params) {
                    val type = TypeGetter(i.declarations.type!!)
                    args.add(
                        ArgObject(
                            IdentifierObject(i.declarations.id.name.length, i.declarations.id.name),
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

            is Statement.IfStatement -> {
                TODO()
            }

            is Statement.ImportStatement -> {
                TODO()
            }

            is Statement.Program -> TODO()
            is Statement.ReturnStatement -> TODO()
            is Statement.SwitchStatement -> TODO()
            is Statement.TryStatement -> TODO()
            is Statement.VariableDeclaration -> TODO()
            is Statement.VariableStatement -> {
                val id = expression(s.declarations.id, pool)
                val array: ArrayList<CodeObject> = arrayListOf()
                if (s.declarations.init != null && s.const) {


                    val iIndex = pool.search(id.toList())
                    array.addAll(statement(Statement.ExpressionStatement(s.declarations.init!!, s.location), pool))
                    typeInvoke(s.declarations.type, pool)?.let { toCode(it) }?.let { array.add(it) }
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
                } else if (!s.const && s.declarations.init == null) {
                    val iIndex = pool.search(id.toList())
                    typeInvoke(s.declarations.type, pool)?.let { toCode(it) }?.let { array.add(it) }
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
                } else if (!s.const && s.declarations.init != null) {
                    val iIndex = pool.search(id.toList())
                    array.addAll(statement(Statement.ExpressionStatement(s.declarations.init!!, s.location), pool))
                    typeInvoke(s.declarations.type, pool)?.let { toCode(it) }?.let { array.add(it) }
                    array.add(toCode(ByteCode.SaveVariable(iIndex!!.toByte())))
                    return array
                } else {
                    thrower.send("Constant Must Init", "notInit", file, s.location, true)
                    return array
                }
            }

            is Statement.WhileStatement -> TODO()
            else -> arrayListOf()
        }
    }

    private fun expression(e: Expression, pool: ConstantPool): ByteArray {
        return when (e) {
            is Expression.AssignmentExpression -> TODO()
            is Expression.BinaryExpression -> {
                TODO()
            }

            is Expression.BooleanLiteral -> TODO()
            is Expression.CallExpression -> {
                val array = ArrayList<Byte>()
                val caller = expression(e.caller, pool)
                array.addAll(caller.toList())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.IDENTIFIER.id, caller)
                }
                array.toByteArray()
            }

            is Expression.GroupExpression -> TODO()
            is Expression.Identifier -> {
                val ret = IdentifierObject(e.name.length, e.name)
                val array = arrayListOf(TypeObject.Type.IDENTIFIER.id)
                array.addAll(ret.toByte().toList())
                if (pool.search(array) == null) {
                    pool.push(TypeObject.Type.IDENTIFIER.id, ret.id.toByteArray())
                }
                array.toByteArray()
            }

            is Expression.LogicalExpression -> TODO()
            is Expression.MemberExpression -> TODO()
            Expression.NullLiteral -> TODO()
            is Expression.NumericLiteral -> {
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

            is Expression.ObjectLiteral -> TODO()
            is Expression.StringLiteral -> {
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

            is Expression.UnaryExpression -> TODO()
            Expression.VoidLiteral -> TODO()
            is Expression.Constructor -> TODO()
            is Expression.ToExpression -> TODO()
        }
    }

    fun variableType(i: Expression, pool: ConstantPool): Byte {
        return when (val init = i) {
            is Expression.StringLiteral -> {
                0x01
            }

            is Expression.NumericLiteral -> {
                0x02
            }

            is Expression.BooleanLiteral -> {
                0x03
            }

            is Expression.Identifier -> {
                val value = expression(i, pool)
                constants.search(value.toList())!!.toByte()
            }

            else -> {
                0x00
            }
        }
    }

    fun TypeGetter(i: Expression): TypeObject {
        return when (val init = i) {
            is Expression.BooleanLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.BOOLEAN, BooleanObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.BOOLEAN)
                }
            }

            is Expression.StringLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.STRING, StringObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.STRING)
                }
            }

            is Expression.Identifier -> {

                TypeObject(TypeObject.Type.IDENTIFIER, IdentifierObject(init.name.length, init.name))
            }

            is Expression.NumericLiteral -> {
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

    fun typeInvoke(expr: Expression?, pool: ConstantPool): ByteCode? {
        if (expr != null) {
            val type = variableType(expr, pool)
            return ByteCode.InvokeType(type)
        }
        return null
    }
}

