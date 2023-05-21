package com.projectsova.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class Card(var id: Int,
                val address: String,
                var arrived: Boolean,
                var time: String?) : Parcelable

