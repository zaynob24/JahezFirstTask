package com.example.jahezfirsttask.presentation.login

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
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.common.Constants.EMPTY_EMAIL
import com.example.jahezfirsttask.common.Constants.EMPTY_PASSWORD
import com.example.jahezfirsttask.common.Constants.INVALID_EMAIL
import com.example.jahezfirsttask.common.Constants.VALID_INPUTS
import com.example.jahezfirsttask.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var  email: String
    private lateinit var  password: String

    private val loginViewModel : LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataValidityState()
        checkUserAuthentication()
        clickListener()
        initCollectFlow()
    }


    private fun checkUserAuthentication() {
        // check if user already loggedIn go to homepage(restaurantListFragment)
        if(loginViewModel.isUserAuthenticated())
        {
            findNavController().navigate(R.id.action_loginFragment_to_restaurantListFragment)
        }
    }

    //Get Restaurant List by Collect Flow
    private fun initCollectFlow() {
        //Login sharedFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginSharedFlow.collectLatest { loginState ->

                    when{

                        loginState.isLoading ->{
                            //show progress Bar
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d(TAG,"is Loading")
                        }

                        loginState.isSuccess ->{
                            //hide progress Bar
                            binding.progressBar.visibility = View.INVISIBLE
                            Log.d(TAG,"login Successfully")
                            findNavController().navigate(R.id.action_loginFragment_to_restaurantListFragment)

                        }

                        loginState.error.isNotBlank() ->{
                            //hide progress Bar
                            binding.progressBar.visibility =  View.INVISIBLE
                            Log.d(TAG,loginState.error)
                            //show error massage
                            Toast.makeText(requireActivity(), loginState.error, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }

    }

    private fun clickListener(){

        //to open Register Fragment
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //Login
        binding.loginButton.setOnClickListener {

            collectDataFromUser() // to collect items data from all fields
            loginViewModel.checkDataValidity(email, password)
        }
    }

    private fun observeDataValidityState() {
        val emailLayout = binding.emailLoginTextField
        val passwordLayout = binding.passwordLoginTextField

        emailLayout.error = null
        passwordLayout.error = null

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.inputState.collect { validState ->

                validState.onEach {
                    when (it) {
                        EMPTY_EMAIL -> emailLayout.error = getString(R.string.required)
                        INVALID_EMAIL -> emailLayout.error = getString(R.string.invalid_email)
                        EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.required)
                        VALID_INPUTS -> {
                            emailLayout.error = null
                            passwordLayout.error = null
                            loginViewModel.login(email, password)
                        }
                    }
                }
            }
        }
    }

    //Collect data from all fields
    private fun collectDataFromUser() {

        email = binding.emailLoginTV.text.toString().trim()
        password = binding.passwordLoginTV.text.toString().trim()

    }

}



