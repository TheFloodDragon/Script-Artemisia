package net.mugwort.artemisia.stdlib

import net.mugwort.artemisia.api.Artemisia
import net.mugwort.artemisia.api.plugins.ArtemisiaPlugin
import net.mugwort.artemisia.stdlib.functions.Console

class Stdlib : ArtemisiaPlugin() {
    override fun initialize() {
        Artemisia.getBus().registerModule(Console())
    }
}