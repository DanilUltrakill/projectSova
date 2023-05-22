package com.projectsova.data

import android.content.Context
import com.yandex.mapkit.MapKitFactory

object MapKitInitializer {
    private var initialized = false
    //private const val apiKey = "2f085e5d-4400-4f49-91fa-be91163cbf86"
    private const val apiKey = "b2f6dc37-186a-4496-949f-e5c4b14a59ba"
    fun initialize(context: Context?) {
        if (initialized) {
            return
        }
        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(context)
        initialized = true
    }
}