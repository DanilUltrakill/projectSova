package com.projectsova.presentation.AddInfo

sealed class StateAddInfo{

    object Initial: StateAddInfo()

    object Loading: StateAddInfo()

    object Content: StateAddInfo()

    object Error: StateAddInfo()
}
