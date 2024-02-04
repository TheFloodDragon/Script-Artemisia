package net.artemisia.utils

import java.time.LocalDate


object Logger {
    enum class Level(id:String){
        /*
            「ALL」: 最低等级的，用于打开所有日志记录。

            「TRACE」 : 很低的日志级别，一般不会使用。

            「DEBUG」:  指出细粒度信息事件对调试应用程序是非常有帮助的，主要用于开发过程中打印一些运行信息。

            「INFO」: 消息在粗粒度级别上突出强调应用程序的运行过程。打印一些你感兴趣的或者重要的信息，这个可以用于生产环境中输出程序运行的一些重要信息，但是不能滥用，避免打印过多的日志。

            「WARN」: 表明会出现潜在错误的情形，有些信息不是错误信息，但是也要给程序员的一些提示。

            「ERROR」: 指出虽然发生错误事件，但仍然不影响系统的继续运行。打印错误和异常信息，如果不想输出太多的日志，可以使用这个级别。

            「FATAL」: 指出每个严重的错误事件将会导致应用程序的退出。这个级别比较高了。重大错误，这种级别你可以直接停止程序了。

            「OFF」:  最高等级的，用于关闭所有日志记录。
         */
        OFF("off"),
        FATAL("Fatal"),
        ERROR("Error"),
        WARNING("Warning"),
        INFO("Info"),
        DEBUG("Debug"),
        TRACE("Trace"),
        ALL("All")
    }
    fun info(text:String){
        println("[${LocalDate.now()}|MScript|${Level.INFO}] $text")
    }
    fun fatal(text:String){
        println("[${LocalDate.now()}|MScript|${Level.FATAL}] $text")
    }

}