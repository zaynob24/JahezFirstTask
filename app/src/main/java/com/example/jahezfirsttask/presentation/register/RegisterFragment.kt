package com.example.jahezfirsttask.presentation.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.base.BaseFragment
import com.example.jahezfirsttask.common.Constants.EMPTY_CONFIRM_PASSWORD
import com.example.jahezfirsttask.common.Constants.EMPTY_EMAIL
import com.example.jahezfirsttask.common.Constants.EMPTY_PASSWORD
import com.example.jahezfirsttask.common.Constants.INVALID_EMAIL
import com.example.jahezfirsttask.common.Constants.INVALID_PASSWORD
import com.example.jahezfirsttask.common.Constants.PASSWORDS_NOT_MATCH
import com.example.jahezfirsttask.common.Constants.VALID_INPUTS
import com.example.jahezfirsttask.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var  email: String
    private lateinit var  password: String
    private lateinit var  confirmPassword: String


    private val registerViewModel : RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener()
        initCollectFlow()
        observeListener()
    }

    private fun init(){
        setBaseViewModel(registerViewModel)
        setUIState()
    }

    private fun initCollectFlow() {

        //Register stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.registerSharedFlow.collectLatest { registerState ->

                    //register Successfully
                    if (registerState) {
                        Log.d(TAG,"Register Successfully")
                        findNavController().navigate(R.id.action_registerFragment_to_restaurantListFragment)
                    }

                }
            }
        }
    }

    private fun clickListener() {
        //to open Login Fragment
        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun observeListener(){
        observeDataValidityState()

        //Register
        binding.signUpButton.setOnClickListener {

            collectDataFromUser() // to collect items data from all fields
            registerViewModel.checkDataValidity(email, password, confirmPassword)// to check if all field contain data and give error massage if not
        }

    }


    private fun observeDataValidityState() {
        val emailLayout = binding.emailSignUpField
        val passwordLayout = binding.passwordSignUpField
        val confirmPassLayout = binding.confirmPasswordSignUpField


        emailLayout.error = null
        passwordLayout.error = null

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            registerViewModel.inputState.collectLatest { validState ->
                Log.d(TAG,"validState: $validState")

                validState.onEach {
                    when (it) {
                        EMPTY_EMAIL -> emailLayout.error = getString(R.string.required)
                        INVALID_EMAIL -> emailLayout.error = getString(R.string.invalid_email)
                        EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.required)
                        EMPTY_CONFIRM_PASSWORD -> confirmPassLayout.error =
                            getString(R.string.required)
                        PASSWORDS_NOT_MATCH -> confirmPassLayout.error =
                            getString(R.string.password_not_match)
                        INVALID_PASSWORD -> passwordLayout.error =
                            getString(R.string.invalid_password_massage)
                        VALID_INPUTS -> {
                            emailLayout.error = null
                            passwordLayout.error = null
                            confirmPassLayout.error = null
                            //register new user
                            registerViewModel.register(email, password)
                        }
                    }
                }
                }
            }
        }
    }

    // to collect post data from all fields
    private fun collectDataFromUser() {
        email = binding.emailSignUpTV.text.toString().trim()
        password = binding.passwordSignUpTV.text.toString().trim()
        confirmPassword = binding.confirmPasswordSignUpTV.text.toString().trim()

    }
}