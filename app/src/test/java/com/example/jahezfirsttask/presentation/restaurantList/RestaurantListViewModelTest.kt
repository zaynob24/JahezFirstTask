package com.example.jahezfirsttask.presentation.restaurantList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.jahezfirsttask.MainCoroutineRule
import com.example.jahezfirsttask.base.BaseViewModel
import com.example.jahezfirsttask.common.Result
import com.example.jahezfirsttask.domain.model.Restaurant
import com.example.jahezfirsttask.domain.repository.IAuthRepository
import com.example.jahezfirsttask.domain.repository.IRestaurantRepository
import com.example.jahezfirsttask.domain.useCase.authentication.SignOutUseCase
import com.example.jahezfirsttask.domain.useCase.restaurantList.GetRestaurantUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RestaurantListViewModelTest {


    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: IRestaurantRepository

    @Mock
    private lateinit var firebaseRepository: IAuthRepository



    private lateinit var viewModel:RestaurantListViewModel

    private lateinit var getRestaurantUseCase : GetRestaurantUseCase
    private lateinit var signOutUseCase: SignOutUseCase

    private lateinit var baseViewModel: BaseViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getRestaurantUseCase = GetRestaurantUseCase(repository)
        signOutUseCase = SignOutUseCase(firebaseRepository)

        viewModel = RestaurantListViewModel(getRestaurantUseCase,signOutUseCase)

        baseViewModel = viewModel

    }

    @Test
    fun `getRestaurant list , return success with data`() = runTest {
         val restaurant = listOf(
             Restaurant(
                 1.9865785830705502,
                 false,
                 "12:00 PM - 03:30 AM",
                 1,
                 "https://jahez-other-oniiphi8.s3.eu-central-1.amazonaws.com/4.jpg",
                 "Pizzaratti",
                 8.0,

             )
         )
        Mockito.`when`(repository.getRestaurant()).thenReturn(flow { emit(Result.Success(restaurant)) })
        viewModel.getRestaurant()


        val job = launch {
            viewModel.stateFlow.test {
                val emission = awaitItem()
                assertThat(emission.size).isEqualTo(1)
                cancelAndConsumeRemainingEvents()

            }
        }

        job.join()
        job.cancel()

    }


    @Test
    fun `getRestaurant list , return success without data`() = runTest {
        val restaurant = emptyList<Restaurant>()
        Mockito.`when`(repository.getRestaurant()).thenReturn(flow { emit(Result.Success(restaurant)) })
        viewModel.getRestaurant()


        val job = launch {
            viewModel.baseUIState.test {
                val emission = awaitItem()
                assertThat(emission.isLoading).isTrue()
                cancelAndConsumeRemainingEvents()

            }
        }

        job.join()
        job.cancel()

    }


    @Test
    fun `getRestaurant list , return is loading`() = runTest {

        Mockito.`when`(repository.getRestaurant()).thenReturn(flow { emit(Result.Loading()) })
        viewModel.getRestaurant()


        val job = launch {
            baseViewModel.baseUIState.test {
                val emission = awaitItem()
                assertThat(emission.isLoading).isTrue()
                cancelAndConsumeRemainingEvents()

            }
        }

        job.join()
        job.cancel()

    }



    @Test
    fun `getRestaurant list , return Error`() = runTest {

        Mockito.`when`(repository.getRestaurant()).thenReturn(flow { emit(Result.Error("Error")) })
        viewModel.getRestaurant()


        val job = launch {
            baseViewModel.baseUIState.test {
                val emission = awaitItem()
                assertThat(emission.error).isNotEmpty()
                cancelAndConsumeRemainingEvents()

            }
        }

        job.join()
        job.cancel()

    }

}