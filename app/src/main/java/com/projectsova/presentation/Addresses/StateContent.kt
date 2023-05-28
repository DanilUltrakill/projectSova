package com.projectsova.presentation.Addresses

sealed class StateContent{

    object Initial: StateContent()

    object Loading: StateContent()

    object Content: StateContent()

    object Error: StateContent()
}
