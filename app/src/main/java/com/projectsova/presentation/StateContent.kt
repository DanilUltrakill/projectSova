package com.projectsova.presentation

sealed class StateContent{

    object Initial: StateContent()

    object Loading: StateContent()

    object Content: StateContent()

    object Error: StateContent()
}
