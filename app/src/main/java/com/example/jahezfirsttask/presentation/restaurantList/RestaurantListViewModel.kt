package com.example.jahezfirsttask.presentation.restaurantList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.domain.state.RestaurantListState
import com.example.jahezfirsttask.domain.useCase.authentication.SignOutUseCase
import com.example.jahezfirsttask.domain.useCase.restaurantList.GetRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.example.jahezfirsttask.common.Result
import kotlinx.coroutines.launch


private const val TAG = "RestaurantListViewModel"
@HiltViewModel
class RestaurantListViewModel  @Inject constructor(
    private val getRestaurantUseCase : GetRestaurantUseCase,
    private val signOutUseCase: SignOutUseCase

):ViewModel() {

    //------------------------------------------authentication----------------------------------------------------//

    //signOut
    fun signOut()= signOutUseCase.invoke()

    //------------------------------------------get Restaurant----------------------------------------------------//

    //state Flow
    private val _stateFlow = MutableStateFlow(RestaurantListState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        Log.d(TAG,"getRestaurant()")
        getRestaurant()
    }
    private fun getRestaurant() {

        viewModelScope.launch {

            //because we use invoke() fun .. we can use class name as function..getRestaurantUseCase()
            getRestaurantUseCase().onEach { result ->

                when (result) {

                    is Result.Success -> {
                        _stateFlow.value = RestaurantListState(restaurant = result.data?: emptyList())
                        Log.d(TAG,result.data.toString())

                    }
                    is Result.Error -> {
                        _stateFlow.value = RestaurantListState(error = result.message?:"An unaccepted error accrue")
                        Log.d(TAG,result.message.toString())

                    }

                    is Result.Loading -> {
                        _stateFlow.value = RestaurantListState(isLoading = true)
                        Log.d(TAG,"laoding")

                    }
                }

            }.launchIn(viewModelScope)

        }
    }
}