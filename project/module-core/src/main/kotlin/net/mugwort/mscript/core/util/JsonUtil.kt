package net.mugwort.mscript.core.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val baseJson: Gson by lazy { GsonBuilder().setPrettyPrinting().create() }