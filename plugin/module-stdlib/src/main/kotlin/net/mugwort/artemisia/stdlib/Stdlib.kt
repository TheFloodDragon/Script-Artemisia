package net.mugwort.artemisia.stdlib

import net.artemisia.api.Artemisia
import net.artemisia.api.plugins.ArtemisiaPlugin
import net.mugwort.artemisia.stdlib.function.Console

class Stdlib : ArtemisiaPlugin() {
    override fun initialize() {
        Artemisia.getBus().registerModule(Console())
    }
}