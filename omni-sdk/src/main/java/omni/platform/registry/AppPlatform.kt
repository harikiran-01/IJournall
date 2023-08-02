package omni.platform.registry

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

object AppPlatform: Platform {
    private val apis: ConcurrentHashMap<Class<*>, ApiImpl<*>> = ConcurrentHashMap()

    internal class ApiImpl<T>(private val init: () -> T) {

        private var _implRef: AtomicReference<T>? = null

        @Synchronized
        fun instance(): T {
            var value = _implRef?.get()
            return if (value == null) {
                value = init.invoke()
                _implRef = AtomicReference(value)
                value
            }
            else {
                value
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Api> getApi(apiClass: Class<Api>): Api? {
        val api = apis[apiClass]
        return api?.instance() as? Api
    }
}