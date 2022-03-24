package com.example.jahezfirsttask.presentation.restaurant_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.use_case.restaurant_list.GetRestaurantUseCase
import com.example.jahezfirsttask.presentation.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class RestaurantListViewModel  @Inject constructor(
    private val getRestaurantUseCase : GetRestaurantUseCase
):ViewModel() {


    //state Flow
    private val _stateFlow = MutableStateFlow(RestaurantListState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        getRestaurant()
    }
    private fun getRestaurant() {
        //because we use invoke() fun .. we can use class name as function..getRestaurantUseCase()
        getRestaurantUseCase().onEach { result ->

            when (result) {

                is Resource.Success -> {
                    _stateFlow.value = RestaurantListState(restaurant = result.data?: emptyList())
                }
                is Resource.Error -> {
                    _stateFlow.value = RestaurantListState(error = result.message?:"An unaccepted error accrue")
                }

                is Resource.Loading -> {
                    _stateFlow.value = RestaurantListState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
}