package com.example.composing.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
