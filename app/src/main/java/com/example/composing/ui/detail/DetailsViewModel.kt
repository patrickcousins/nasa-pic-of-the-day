package com.example.composing.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.composing.ui.home.Apod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel  @Inject constructor(
    val handle: SavedStateHandle,
) : ViewModel() {

    private val _detailsSateFlow = MutableStateFlow<DetailsUiState?>(null)
    val detailsSateFlow: StateFlow<DetailsUiState?> get() = _detailsSateFlow

    fun load(apod: Apod) {
        _detailsSateFlow.value = DetailsUiState(
            copyright = apod.copyright,
            date = apod.date,
            explanation = apod.explanation,
            title = apod.title,
            url = apod.url,
        )
        handle["apod"] = apod
    }

}