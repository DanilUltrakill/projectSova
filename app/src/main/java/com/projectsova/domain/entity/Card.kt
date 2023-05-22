package com.projectsova.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(var id: Int,
                val address: String,
                var arrived: Boolean,
                var time: String?) : Parcelable

