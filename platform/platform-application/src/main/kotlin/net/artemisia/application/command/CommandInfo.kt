package net.artemisia.application.command

@Target(AnnotationTarget.CLASS)
annotation class CommandInfo(val name: String, val info: String, val use: String = "")