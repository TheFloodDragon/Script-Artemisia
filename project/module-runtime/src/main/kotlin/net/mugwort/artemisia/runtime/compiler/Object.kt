package net.mugwort.artemisia.runtime.compiler

abstract class Object {
   fun createArray(): ArrayList<Byte> {
      return ArrayList()
   }
   abstract fun toByte() : ByteArray
}