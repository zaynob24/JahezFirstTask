package com.example.jahezfirsttask.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide.init
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.common.Constants
import com.example.jahezfirsttask.common.Constants.EMPTY_CONFIRM_PASSWORD
import com.example.jahezfirsttask.common.Constants.EMPTY_EMAIL
import com.example.jahezfirsttask.common.Constants.EMPTY_PASSWORD
import com.example.jahezfirsttask.common.Constants.INVALID_EMAIL
import com.example.jahezfirsttask.common.Constants.INVALID_PASSWORD
import com.example.jahezfirsttask.common.Constants.PASSWORDS_NOT_MATCH
import com.example.jahezfirsttask.common.Constants.VALID_INPUTS
import com.example.jahezfirsttask.databinding.FragmentRegisterBinding
import com.example.jahezfirsttask.presentation.util.Validations
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding


    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText

    private val registerViewModel : RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        clickListener()
        initCollectFlow()
        observeListener()
    }

    private fun init() {
        email = binding.emailSignUpTV
        password = binding.passwordSignUpTV
    }



    private fun initCollectFlow() {

        //Register stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.registerSharedFlow.collectLatest { registerState ->

                    when{

                        registerState.isLoading ->{
                            //show progress Bar
                            binding.progressBarRegister.visibility = View.VISIBLE
                            Log.d(TAG,"is Loading")
                        }

                        registerState.isSuccess ->{
                            //hide progress Bar
                            binding.progressBarRegister.visibility = View.INVISIBLE
                            Log.d(TAG,"Register Successfully")
                            findNavController().navigate(R.id.action_registerFragment_to_restaurantListFragment)

                        }

                        registerState.error.isNotBlank() ->{
                            //hide progress Bar
                            binding.progressBarRegister.visibility =  View.INVISIBLE
                            Log.d(TAG,registerState.error)
                            //show error massage
                            Log.d(TAG," registerState.error.isNotBlank() ${registerState.error}")
                            Toast.makeText(requireActivity(), registerState.error, Toast.LENGTH_SHORT).show()
                        }

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

            //collectDataFromUser() // to collect items data from all fields
            Log.d(TAG,"signUpButton.setOnClickListener .. email: ${ email.text.toString().trim()} pass: ${password.text.toString().trim()} ")
            val confirmPassword = binding.confirmPasswordSignUpTV.text.toString()
            registerViewModel.checkDataValidity(
                email.text.toString().trim(),
                password.text.toString().trim(),
                confirmPassword)// to check if all field contain data and give error massage if not
        }

    }


    private fun observeDataValidityState() {
        val emailLayout = binding.emailSignUpField
        val passwordLayout = binding.passwordSignUpField
        val confirmPassLayout = binding.confirmPasswordSignUpField


        emailLayout.error = null
        passwordLayout.error = null

        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.inputState.collectLatest { validState ->
                Log.d(TAG,"validState: $validState")

                validState.onEach {
                    when (it) {
                        EMPTY_EMAIL -> emailLayout.error = getString(R.string.required)
                        INVALID_EMAIL -> emailLayout.error = getString(R.string.invalid_email)
                        EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.required)
                        EMPTY_CONFIRM_PASSWORD -> confirmPassLayout.error = getString(R.string.required)
                        PASSWORDS_NOT_MATCH -> confirmPassLayout.error = getString(R.string.password_not_match)
                        INVALID_PASSWORD -> passwordLayout.error = getString(R.string.invalid_password_massage)
                        VALID_INPUTS -> {
                            emailLayout.error = null
                            passwordLayout.error = null
                            confirmPassLayout.error = null
                            //register new user
                            Log.d(TAG,"email: ${email.text.toString().trim()} pass: ${password.text.toString().trim()}")
                            registerViewModel.register(email.text.toString().trim(),
                                password.text.toString().trim())
                        }
                    }
                }
            }
        }
    }

    // to collect post data from all fields
    private fun collectDataFromUser() {

//        email = binding.emailSignUpTV.text.toString().trim()
//        password = binding.passwordSignUpTV.text.toString().trim()
//        confirmPassword = binding.confirmPasswordSignUpTV.text.toString().trim()

        Log.d(TAG,"collectDataFromUser() .. email: $email pass: $password")


    }
}