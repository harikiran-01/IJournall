package omni.platform.registry

interface Platform {
    fun <Api> getApi(apiClass: Class<Api>): Api?
}