package net.artemisia.utils

import java.io.File

class FileUtils {
    private val files: ArrayList<File>

    init {
        files = ArrayList()
    }

    fun finder(file: File): Array<out File>? {
        return file.listFiles()
    }

    fun finder(path: String): Array<out File>? {
        return File(path).listFiles()
    }

    fun getFiles(path: String): ArrayList<File> {
        val files = finder(path)
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    getFiles(file.path)
                }
                this.files.add(file)
            }
        }

        return this.files
    }

    fun getFiles(file: File): ArrayList<File> {
        val files = finder(file)
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    getFiles(file)
                }
                this.files.add(file)
            }
        }

        return this.files
    }
}