package com.example.composing.ui.detail

import com.example.composing.ui.home.Apod
import dagger.assisted.AssistedFactory

//@AssistedFactory
interface DetailsViewModelFactory {
    fun create(apod: Apod): DetailsViewModel
}