package net.mugwort.artemisia.api.module

import net.mugwort.artemisia.api.Environment

abstract class Module(val id:String) {
    val env = Environment()
    abstract fun ModuleEnv() : Environment
}