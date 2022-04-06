package com.example.jahezfirsttask.presentation.restaurantList

import androidx.lifecycle.viewModelScope
import com.example.jahezfirsttask.base.BaseViewModel
import com.example.jahezfirsttask.domain.useCase.authentication.SignOutUseCase
import com.example.jahezfirsttask.domain.useCase.restaurantList.GetRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.model.Restaurant
import com.example.jahezfirsttask.domain.state.BaseUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


private const val TAG = "RestaurantListViewModel"
@HiltViewModel
class RestaurantListViewModel  @Inject constructor(
    private val getRestaurantUseCase : GetRestaurantUseCase,
    private val signOutUseCase: SignOutUseCase,

):BaseViewModel() {

    //authentication
    //signOut
    fun signOut()= signOutUseCase.invoke()

    //get Restaurant

    //state Flow
    private val _stateFlow = MutableStateFlow<List<Restaurant>>(emptyList())
    val stateFlow = _stateFlow.asStateFlow()

     fun getRestaurant() {

        viewModelScope.launch(Dispatchers.IO) {

            //because we use invoke() fun .. we can use class name as function..getRestaurantUseCase()
            getRestaurantUseCase().onEach { result ->

                when (result) {

                    is Result.Loading -> {

                        _baseUIState.emit(BaseUIState(isLoading = true))
                        //Log.d(TAG,"loading")


                    }

                    is Result.Error -> {
                        _baseUIState.emit(BaseUIState(error = result.message?:"An unaccepted error accrue"))
                        //Log.d(TAG,result.message.toString())

                    }

                    is Result.Success -> {
                        _stateFlow.value = result.data?: emptyList()
                        _baseUIState.emit(BaseUIState())
                        //Log.d(TAG,result.data.toString())

                    }

                }

            }.launchIn(viewModelScope)

        }
    }
}