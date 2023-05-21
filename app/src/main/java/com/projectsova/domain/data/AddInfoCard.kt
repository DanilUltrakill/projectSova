package com.projectsova.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddInfoCard(val phone: String,
                       val products: String) : Parcelable
