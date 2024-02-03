package net.mugwort.artemisia.spawner.compiler

import net.mugwort.artemisia.api.expection.thrower
import net.mugwort.artemisia.core.asm.ByteCode
import net.mugwort.artemisia.core.ast.core.Expression
import net.mugwort.artemisia.core.ast.core.Statement
import net.mugwort.artemisia.runtime.compiler.ConstantPool
import net.mugwort.artemisia.runtime.compiler.objects.*
import net.mugwort.artemisia.spawner.Parser
import java.io.File



class Compiler(private val file : File) {
    private val body = Parser(file.readText(),file).parser().body
    private val variables : ArrayList<Statement.VariableStatement> = arrayListOf()
    private val functions : ArrayList<Statement.FunctionDeclaration> = arrayListOf()
    private val constants = ConstantPool()
    private val codes : ArrayList<CodeObject> = arrayListOf()
    private val funcs : ArrayList<FunctionObject> = arrayListOf()
    init {
        for (obj in body){
            if (obj is Statement.VariableStatement) variables.add(obj)
            if (obj is Statement.FunctionDeclaration) functions.add(obj)
        }
    }
    fun save(){
        for (i in variables){
            codes.addAll(statement(i))
        }
        for(f in functions){
            codes.addAll(statement(f))
        }
        val compiled = File(file.parentFile.path + "/" + file.nameWithoutExtension + ".apc")
        val obj = ModuleObject(byteArrayOf(0x00,0x00,0x30,0x00),constants,codes,funcs,file.absolutePath.toString().toByteArray())
        compiled.writeBytes(obj.toByte())
        compiled.createNewFile()
    }

    fun statement(s : Statement) : ArrayList<CodeObject> {
        return when(s){
            is Statement.BlockStatement -> {
                val array : ArrayList<CodeObject> = arrayListOf()
                for (i in s.body){
                    array.addAll(statement(i))
                }
                array
            }
            is Statement.CaseDeclaration -> TODO()
            is Statement.ClassDeclaration -> TODO()
            is Statement.DoWhileStatement -> TODO()
            is Statement.EmptyStatement -> TODO()
            is Statement.EnumStatement -> TODO()
            is Statement.EventStatement -> TODO()
            is Statement.ExpressionStatement -> TODO()
            is Statement.ForStatement -> TODO()
            is Statement.FunctionDeclaration -> {
                val args : ArrayList<ArgObject> = arrayListOf()
                val array : ArrayList<CodeObject> = arrayListOf()
                val id = TypeGetter(s.identifier)
                for (i in s.params){
                    val type = TypeGetter(i.declarations.init!!)
                    if(type.obj != null){
                        args.add(
                            ArgObject(
                                IdentifierObject(i.declarations.id.name.length,i.declarations.id.name),
                                type,
                                type.obj!!
                            )
                        )
                    }else{
                        thrower.send("Not Params Type","NoType",file,s.location)
                    }
                }
                val codes = statement(s.body!!)
                val returnType = TypeGetter(s.returnValue)
                val func = FunctionObject((id.obj as IdentifierObject),args,codes,returnType)
                funcs.add(func)
                array.add(toCode(ByteCode.CreateFunction(funcs.indexOf(func).toByte())))
                array
            }
            is Statement.IfStatement -> TODO()
            is Statement.ImportStatement -> TODO()
            is Statement.Program -> TODO()
            is Statement.ReturnStatement -> TODO()
            is Statement.SwitchStatement -> TODO()
            is Statement.TryStatement -> TODO()
            is Statement.VariableDeclaration -> TODO()
            is Statement.VariableStatement -> {
                val id = expression(s.declarations.id)
                val value = expression(s.declarations.init!!)
                val vIndex = constants.search(value.toList())
                val iIndex = constants.search(id.toList())

                val array : ArrayList<CodeObject> = arrayListOf()
                array.add(toCode(ByteCode.Push(vIndex!!.toByte())))
                array.add(toCode(ByteCode.SaveItem(iIndex!!.toByte())))
                array
            }
            is Statement.VisitorStatement -> TODO()
            is Statement.WhileStatement -> TODO()
        }
    }

    fun expression(e: Expression): ByteArray {
        return when(e){
            is Expression.AssignmentExpression -> TODO()
            is Expression.BinaryExpression -> {
                TODO()
            }
            is Expression.BooleanLiteral -> TODO()
            is Expression.CallExpression -> TODO()
            is Expression.GroupExpression -> TODO()
            is Expression.Identifier -> {
                val ret = IdentifierObject(e.name.length,e.name)
                constants.push(TypeObject.Type.IDENTIFIER.id,ret.id.toByteArray())
                val array = arrayListOf(TypeObject.Type.IDENTIFIER.id)
                array.addAll(ret.toByte().toList())
                array.add(1,ret.len.toByte())
                array.toByteArray()
            }
            is Expression.LogicalExpression -> TODO()
            is Expression.MemberExpression -> TODO()
            Expression.NullLiteral -> TODO()
            is Expression.NumericLiteral -> {
                val ret = NumberObject(e.value!!)
                val array = ArrayList<Byte>()

                array.addAll(ret.toByte().toList())
                constants.push(TypeObject.Type.NUMBER.id,array.toByteArray())
                array.add(0,TypeObject.Type.NUMBER.id)
                array.add(1,ret.getLen())
                array.toByteArray()
            }
            is Expression.ObjectLiteral -> TODO()
            is Expression.StringLiteral -> TODO()
            is Expression.UnaryExpression -> TODO()
            Expression.VoidLiteral -> TODO()
        }
    }

    fun TypeGetter(i : Expression): TypeObject {
       return when(val init = i){
            is Expression.BooleanLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.BOOLEAN,BooleanObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.BOOLEAN)
                }
            }

            is Expression.StringLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.STRING,StringObject(init.value!!))
                } else {
                    TypeObject(TypeObject.Type.STRING)
                }
            }
            is Expression.Identifier -> {
                TypeObject(TypeObject.Type.IDENTIFIER,IdentifierObject(init.name.length,init.name))
            }
            is Expression.NumericLiteral -> {
                if (init.value != null) {
                    TypeObject(TypeObject.Type.NUMBER,NumberObject(init.value!!))
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
        return CodeObject(command)
    }
    fun addFunction(){

    }
}

