package net.mugwort.artemisia.api.register

class RegisterObject<T> {
    private val registry: MutableMap<String, T> = HashMap()
    fun register(key: String, value: T) {
        registry[key] = value
    }

    fun get(key: String): T? {
        return registry[key]
    }

    fun contains(key: String): Boolean {
        return registry.containsKey(key)
    }

    fun unregister(key: String) {
        registry.remove(key)
    }
}