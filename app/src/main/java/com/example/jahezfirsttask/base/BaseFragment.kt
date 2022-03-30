package com.example.jahezfirsttask.base

import android.util.Log
import androidx.fragment.app.Fragment

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch


private const val TAG = "BaseFragment"
abstract class BaseFragment : Fragment() {

    lateinit var mViewModel: BaseViewModel


    protected fun setBaseViewModel(baseViewModel: BaseViewModel) {
        mViewModel = baseViewModel
    }


    // Base UI States
    protected fun setUIState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            mViewModel.baseUIState.collect { state ->
                Log.d(TAG, "Base state: $state")
                when {
                    state.isLoading -> {
                        Log.d(TAG,"state.isLoading ${state.isLoading}")
                        showProgressBar(true)
                    }
                    state.error.isNotBlank() -> {
                        Log.d(TAG,"state.error ${state.error}")
                        showProgressBar(false)
                        Toast.makeText(requireActivity(), state.error, Toast.LENGTH_LONG).show()
                    }
                    else -> showProgressBar(false)
                }
            }}
        }
    }

    private fun showProgressBar(state: Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.showProgressBar(state)
    }

}