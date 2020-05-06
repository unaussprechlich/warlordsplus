package net.unaussprechlich.eventbus

interface IEvent


object EventBus {

    private val busMap = mutableMapOf<String, Bus<*>>()

    @Suppress("UNCHECKED_CAST")
    private class Bus<T : IEvent> {

        private val subscribers = ArrayList<(T) -> Unit>()

        fun <E : IEvent> register(callback: (E) -> Unit) {
            subscribers.add(callback as (T) -> Unit)
        }

        fun <E : IEvent> unregister(callback: (E) -> Unit) {
            subscribers.remove(callback as (T) -> Unit)
        }

        fun <E : IEvent> post(data: E) {
            subscribers.forEach { it.invoke(data as T) }
        }

    }

    /**
     * Inline function to be able to do EventBus.register<MyBus>({ })
     */
    inline fun <reified T : IEvent> register(noinline callback: (T) -> Unit) =
            register(T::class.java, callback)

    /**
     * Inline function to be able to do EventBus.unregister<MyBus>({ })
     */
    inline fun <reified T : IEvent> unregister(noinline callback: (T) -> Unit) =
            unregister(T::class.java, callback)

    /**
     * Inline function to be able to do EventBus.post<MyBus>({ })
     */
    inline fun <reified T : IEvent> post(data: T) =
            post(T::class.java, data)

    /**
     * Registers a Callback in a Bus and creates the Bus if it doesn't exist.
     */
    fun <T : IEvent> register(clazz: Class<T>, callback: (T) -> Unit) {
        if (!busMap.containsKey(clazz.toString())) {
            busMap[clazz.toString()] = Bus<T>()
        }

        busMap[clazz.toString()]!!.register(callback)
    }

    /**
     * Unregister a Callback in a Bus and throws a NoSuchElementException if the Bus if it doesn't exist.
     */
    @Throws(NoSuchElementException::class)
    fun <T : IEvent> unregister(clazz: Class<T>, callback: (T) -> Unit) {
        if (!busMap.containsKey(clazz.toString())) {
            throw NoSuchElementException("Can't find Bus for event ${clazz}?")
        }

        busMap[clazz.toString()]!!.unregister(callback)
    }

    /**
     * Post to a Bus and creates the Bus if it doesn't exist.
     */
    fun <T : IEvent> post(clazz: Class<T>, data: T) {
        if (!busMap.containsKey(clazz.toString())) {
            busMap[clazz.toString()] = Bus<T>()
        }

        busMap[clazz.toString()]!!.post(data)
    }
}