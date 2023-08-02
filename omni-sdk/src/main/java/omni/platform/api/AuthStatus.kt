package omni.platform.api

sealed class AuthStatus {
    data class LoggedIn(
        val username: String,
        val uid: Long,
    ) : AuthStatus()

    data class LoggedOut(
        val uid: Long? = 0
    ) : AuthStatus()
}