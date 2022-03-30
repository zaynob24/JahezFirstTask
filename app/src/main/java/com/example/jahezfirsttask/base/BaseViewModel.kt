package com.example.jahezfirsttask.base

import androidx.lifecycle.ViewModel
import com.example.jahezfirsttask.domain.state.BaseUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel  @Inject constructor() : ViewModel() {

    protected val _baseUIState = MutableSharedFlow<BaseUIState>()
    val baseUIState = _baseUIState.asSharedFlow()

}