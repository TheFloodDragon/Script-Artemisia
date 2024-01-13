package net.mugwort.artemisia.application


open class Artemisia {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            Console().init()


        //Interpreter(File(System.getProperty("user.dir") + "/scripts/Main.ari")).execute()
        }
    }
}