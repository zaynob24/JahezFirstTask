package com.example.jahezfirsttask.presentation.restaurant_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.common.Resource
import com.example.jahezfirsttask.domain.use_case.restaurant_list.GetRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class RestaurantListViewModel  @Inject constructor(
    private val getRestaurantUseCase : GetRestaurantUseCase
):ViewModel() {

    private val _state = mutableStateOf(RestaurantListState())
    val state: State<RestaurantListState> = _state

    init {
        getRestaurant()
    }
    private fun getRestaurant() {
        //because we use invoke() fun .. we can use class name as function..getRestaurantUseCase()
        getRestaurantUseCase().onEach { result ->

            when (result) {

                is Resource.Success -> {
                    _state.value = RestaurantListState(restaurant = result.data?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = RestaurantListState(error = result.message?:"An unaccepted error accrue")
                }

                is Resource.Loading -> {
                    _state.value = RestaurantListState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }
}