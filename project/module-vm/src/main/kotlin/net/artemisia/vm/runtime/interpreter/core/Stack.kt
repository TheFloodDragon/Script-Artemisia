package vm.runtime.interpreter.core

class Stack<T> {
    val stack: ArrayList<T> = ArrayList()

    fun push(element: T) {
        stack.add(element)
    }

    fun pop(): T? {
        if (isEmpty()) {
            return null
        }
        return stack.removeAt(stack.size - 1)
    }
    fun top() : T?{
        if (isEmpty()) {
            return null
        }
        return stack.removeAt(0)
    }
    fun peek(): T? {
        if (isEmpty()) {
            return null
        }
        return stack[stack.size - 1]
    }

    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }

    fun size(): Int {
        return stack.size
    }

    fun clear() {
        stack.clear()
    }
}