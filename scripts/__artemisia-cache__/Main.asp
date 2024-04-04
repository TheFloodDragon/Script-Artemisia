[Main -> D:\Project\Artemisia\scripts\Main.ap]
.data
   DT 0 : Auto
   DT 1 : a
   DT 2 : add
   DI 3 : 1
   DI 4 : 2
   DT 5 : Void
   DT 6 : String
   DT 7 : b
   DT 8 : Int
   DT 9 : i


.main
   IV $1,$0      #设定变量 a 类型为 Auto
   PUSH $3      #压入参数 1
   PUSH $4      #压入参数 2
   CALL $2      #加载方法 add 并压入栈
   MOVL $1      #将栈顶值移入 a
   IM %$2      #新建方法
   END       #执行结束


.method $2 ($1,$7) -> $5  #定一个名为$2 的方法
   SP $2,$6      #设定方法参数 a 类型为 String
   MOVL $2      #将栈顶值移入 a
   SP $2,$8      #设定方法参数 b 类型为 Int
   MOVL $2      #将栈顶值移入 b
   IV $1,$0      #设定变量 a 类型为 Auto
   PUSH %i      #压入量 i
   MOVL $1      #将栈顶值移入 a



