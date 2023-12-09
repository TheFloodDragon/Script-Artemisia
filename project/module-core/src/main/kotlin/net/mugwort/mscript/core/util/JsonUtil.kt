package net.mugwort.mscript.core.util

import com.google.gson.GsonBuilder

val baseJson by lazy { GsonBuilder().setPrettyPrinting().create() }