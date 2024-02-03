package net.mugwort.artemisia.application.command

abstract class Command {
    // 执行命令，参数为字符串列表
    abstract fun execute(args: List<String>): Int
}