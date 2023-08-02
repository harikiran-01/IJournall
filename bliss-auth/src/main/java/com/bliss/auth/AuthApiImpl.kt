package com.bliss.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import omni.platform.api.AuthStatus
import omni.platform.auth.api.AuthApi

object AuthApiImpl : AuthApi {

    private val _authStatus = MutableStateFlow<AuthStatus>(AuthStatus.LoggedOut())
    override val authStatus: StateFlow<AuthStatus> = _authStatus

}