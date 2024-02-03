package net.mugwort.artemisia.runtime.compiler

class Stack<E>(private val size : Int) {
    private var array: ArrayList<E> = arrayListOf()
    private var top = -1

    fun push(j: E) {
       array[++top] = j
    }

    fun pop(): E {
        return array[top--]
    }

    fun peek(): E {
        return array[top]
    }

    fun isEmpty(): Boolean {
        return top == -1
    }

    fun isFull(): Boolean {
        return top == size - 1
    }

}