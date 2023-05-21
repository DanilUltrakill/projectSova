package com.projectsova.presentation

sealed class StateAddInfo{

    object Initial: StateAddInfo()

    object Loading: StateAddInfo()

    object Content: StateAddInfo()

    object Error: StateAddInfo()
}
