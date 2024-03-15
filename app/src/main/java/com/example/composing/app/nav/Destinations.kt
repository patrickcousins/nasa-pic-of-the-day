package com.example.composing.app.nav

import android.os.Parcelable
import com.example.composing.ui.home.Apod
import kotlinx.parcelize.Parcelize

sealed class Destinations : Parcelable {
    @Parcelize
    data object Home : Destinations()

    @Parcelize
    data class Details(
        val apod: Apod,
    ) : Destinations()
}