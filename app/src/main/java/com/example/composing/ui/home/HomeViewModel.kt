package com.example.composing.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composing.app.logging.debug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val handle: SavedStateHandle,
    val repo: ApodRepo
) : ViewModel() {


    private val _greetingStateFlow = MutableStateFlow<List<Apod>>(listOf())
    val apodListStateFlow: StateFlow<List<Apod>> get() = _greetingStateFlow

    fun loadApod() {

        if (_greetingStateFlow.value.isEmpty()) {
            viewModelScope.launch {
                debug("loading from repo")
                val list = repo.get(
                    ApodRepo.LoadApodSpec(
                        count = 10
                    )
                )
                debug("list = $list")
                _greetingStateFlow.value = list
            }
        }
    }
}