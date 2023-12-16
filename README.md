# MScript

#### 一个轻量化的脚本框架

# 前言

## · 什么是MScript?

> MScript原名: Mugwort Script 是一个轻量化解析型动态语言
>
> 项目于2023年11月8日开始编写
>
> 当前基于Kotlin
>
> 企划后期增加多语言支持与交互

## · 相关环境部署

无

# 入门

1. ## 基础数据类型

   对象 **Object**

    ``` 
    #*
        他可以是任何东西
    *#
    ```

   字符串 **String : **<u>Object</u>**

    ``` 
    #*
        Object的衍生
    *#
    "String"
    ```

   数字 **Number** : **<u>Object</u>**

    ``` 
    #*
        Object的衍生
    *#
    1
    -1.1
    ```

   布尔值 **Boolean** ：**<u>Object</u>**

    ``` 
    #*
        Object的衍生
    *#
    true
    false
    ```
2. ## 语句

    1. 调用语句

        ``` 
        (预调用的名称)(参数)
        ```
    2. 赋值语句

       > 符号:
       >
       >     = : 普通赋值
       >
       >     %= : 等价于 a = a % b
       >
       >     += : 等价于 a = a + b
       >
       >     -= : 等价于 a = a - b
       >
       >     *= : 等价于 a = a * b
       >
       >     /= : 等价于 a = a / b
       >

        ``` 
            (预赋值) (= | += | -= | %= | *= | /=) (值)
        ```
    3. 标识符语句

        ``` 
        #*

            如你所见 这就是个标识符

        *#
        a
        ```
    4. 二元语句

       就是加减乘除取余

        ``` 
        1 + 1
        1 - 1
        1 * 2
        1 / 3
        1 % 3
        ```

    5. 一元语句

       就是取反自增自减

        ``` 
        !true
        -1
        i++
        ++i
        i--
        --i
        ```

    6. 逻辑语句

       就是逻辑 更其他语言差不多

        ``` 
        && 与
        || 或
        < 小于
        > 大于
        >= 大于等于
        <= 小于等于
        ! 非
        ```

    7. 成员语句

       其实就是调用

        ``` 
        a.b() #这就是一个成员语句
        a["b"] #也是成员语句
        ```

3. ## 表达式

    1. do && whlie逻辑循环

       声明 **Statement**

        ``` 
        do{
            [语句]
        }while(条件)
        ```

       示例 **Example**

        ``` 
        do{
            println(114514)
        }while(true)
        ```
    2. while循环

       声明 **Statement**

        ``` 
        while (条件){
            [语句]
        }
        ```

       示例 **Example**

        ``` 
        while(true){
            println(114514)
        }
        ```
    3. for 循环

        1. 主要的 **for**

           声明 **Statement**

            ``` 
            for((头);(中);(尾)){
                [表达式]
            }
            ```

           示例 **Example**

            ``` 
            for(i = 0;i <= 1;i++){
                println("不知道!!!!!")
            }
            ```

        2. 衍生 **forin**

           声明 **Statement**

            ``` 
            forin ((巴拉巴拉) in (巴拉巴拉)){
                [表达式]
            }
            ```

           示例 **Example**

            ``` 
            forin (i in 不知道!){
                println(i)
            }
            ```
    4. if 逻辑

       声明 **Statement**

        ``` 
        if (条件) { 
            [语句] 
        }
        ```

       示例 **Example**

        ``` 
        if(true){
            println(114514)
        }
        ```
    5. 变量定义

       声明 **Statement**

        ``` 
        (const) (const | val | var) (名称) = [值]
        ```

       示例 **Example**

        ``` 
        #常量
        const a = 114514
        #也可以这样定义
        val a = 114514
        #也可以是
        const val a = 114514
        #变量
        var a = 114514
        ```
    6. 方法定义

       声明 **Statement**

        ``` 
        fn (名称) (参数) : [返回类型] { 
            [表达式]
        }
        ```

       示例 **Example**

        ``` 
        fn helloworld() {
            println("HelloWorld!")
        }
        fn add(a : Number,b : Number){
            return a + b
        }
        ```

    7. 块

       声明 **Statement**

        ``` 
        { 
            [表达式]
        }
        ```

       示例 **Example**

        ``` 
        {
            114514!!!!!
        }
        ```

    8. 模块化

       声明 **Statement**

        ``` 
        import (方位)
        ```

       示例 **Example**

        ``` 
        import core.stdlib
        ```

    9. 类定义

       声明 **Statement**

        ``` 
        class (名称) [参数]{ 
            [表达式]
        }
        ```

       示例 **Example**

        ``` 
        class 臭死了(){
            114514!!!!!
        }
        ```

    10. 事件表达式

        声明 **Statement**

         ``` 
         event (名称){ 
             [表达式]
         }
         ```

        示例 **Example**

         ``` 
         event onPrint(){
             println("print!")
         }
         ```

    11. Switch表达式

        声明 **Statement**

         ``` 
         switch (参数){ 
             case (条件) : {
                 [表达式]
             }
         }
         ```

        示例 **Example**

         ``` 
         switch(a){
             case (1) : {
                 println("a = 1")
             }
         }
         ```

    12. Try表达式

        声明 **Statement**

         ``` 
         try{
             [表达式]
         }(catch(参数) | finally){
             [表达式]
         }[finally{ [表达式] }]
         ```

        示例 **Example**

         ``` 
         try{
             不知道写什么()
         }catch(e : 不知道写什么){
             println(e.message)
         }
         ```
    13. 访问类型表达式

        声明 **Statement**

         ``` 
         (public | private) (表达式)
         ```

        示例 **Example**

         ``` 
         public val a = 1
         private val a = 1
         ```

    14. 返回表达式

        声明 **Statement**

         ``` 
         return (表达式)
         ```

        示例 **Example**

         ``` 
         fn add(){
             return 1 + 1
         }
         ```

    15. 空表达式

        声明 **Statement**

         ```
         ;
         ```

        示例 **Example**

         ```
         ;
         ```





# 构建
**MacOS** / **Linux**
````
    ./gradlew build
````

**Windows**
````
    gradlew.bat buid
````

# 开发人员

主框架编写 ：艾乐([Mugwort](https://gitee.com/aile123))

项目框架 : 蛟龙([TheFloodDragon](https://gitee.com/TheFloodDragon))
