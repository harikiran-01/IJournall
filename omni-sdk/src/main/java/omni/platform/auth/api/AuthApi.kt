package omni.platform.auth.api

import kotlinx.coroutines.flow.StateFlow
import omni.platform.api.AuthStatus

interface AuthApi {

    val authStatus : StateFlow<AuthStatus>

    val isLoggedIn : Boolean get() = authStatus.value is AuthStatus.LoggedIn

    val uid: Long get() = (authStatus.value as AuthStatus.LoggedIn).uid
}