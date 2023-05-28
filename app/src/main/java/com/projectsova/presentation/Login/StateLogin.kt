package com.projectsova.presentation.Login

sealed class StateLogin {

    object Initial: StateLogin()

    object Authorization: StateLogin()

    object Authorized: StateLogin()

    object GettingPassword: StateLogin()

    object NotAuthorized: StateLogin()

    object Error: StateLogin()
}
