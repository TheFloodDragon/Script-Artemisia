package net.artemisia.api.module

import net.artemisia.api.Environment

abstract class Module(val id:String) {
    val env = Environment()
    abstract fun ModuleEnv() : Environment
}