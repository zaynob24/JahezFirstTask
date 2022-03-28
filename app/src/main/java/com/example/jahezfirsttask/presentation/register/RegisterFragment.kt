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
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.databinding.FragmentRegisterBinding
import com.example.jahezfirsttask.presentation.util.Validations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var  name: String
    private lateinit var  email: String
    private lateinit var  password: String
    private lateinit var  confirmPassword: String

    private val validator = Validations()
    private val RegisterViewModel : RegisterViewModel by activityViewModels()

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

        //------------------------------------------------nave to Login Fragment ------------------------------------------------------------//

        //to open Login Fragment
        binding.loginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

        //------------------------------------------------Register------------------------------------------------------------//

        //Register
        binding.signUpButton.setOnClickListener {

            collectDataFromUser() // to collect items data from all fields
            if (checkDataValidity()){ // to check if all field contain data and give error massage if not

                //call firebase register
                RegisterViewModel.Register(email, password)

            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
            }
        }


        //Register stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                RegisterViewModel.stateFlow.collectLatest { registerState ->

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
                            Toast.makeText(requireActivity(), registerState.error, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }


    //------------------------------------------------collectData------------------------------------------------------------//

    // to collect post data from all fields
    private fun collectDataFromUser() {

        name = binding.fullNameSignUpTV.text.toString().trim()
        email = binding.emailSignUpTV.text.toString().trim()
        password = binding.passwordSignUpTV.text.toString().trim()
        confirmPassword = binding.confirmPasswordSignUpTV.text.toString().trim()

    }

    //--------------------------------------------------check Data Validity------------------------------------------------------------//

    // to check if all field contain data and give error massage if not
    private fun checkDataValidity() : Boolean {
        var isAllDataFilled = true

        //check name
        if (name.isEmpty() || name.isBlank()) {
            binding.fullNameSignUpFiled.error = getString(R.string.required)
            isAllDataFilled = false
        } else {
            binding.fullNameSignUpFiled.error = null
        }


        //check email
        if (email.isEmpty() || email.isBlank()) {
            binding.emailSignUpField.error = getString(R.string.required)
            isAllDataFilled = false
        } else {
            //check email validate
            if (validator.emailIsValid(email)){
                binding.emailSignUpField.error = null

            }else{

                isAllDataFilled = false
                binding.emailSignUpField.error = getString(R.string.invalid_email)
                Log.d(TAG,"invalid_email")
            }
        }

        //check password
        if (password.isEmpty() || password.isBlank()) {
            binding.passwordSignUpField.error = getString(R.string.required)
            isAllDataFilled = false

        } else{

            if(validator.passwordIsValid(password)){
                binding.passwordSignUpField.error = null

            }else{

                //check password validate
                isAllDataFilled = false
                binding.passwordSignUpField.error = getString(R.string.invalid_password_massage)
                Log.d(TAG,"invalid_password_massage")
            }

        }

        //check confirmPassword
        if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
            binding.confirmPasswordSignUpField.error = getString(R.string.required)
            isAllDataFilled = false

        }else if(password != confirmPassword){
            binding.confirmPasswordSignUpField.error = getString(R.string.password_not_match)
            isAllDataFilled = false
            Log.d(TAG,"password_not_match")

        }else {
            binding.confirmPasswordSignUpField.error = null
        }

        return isAllDataFilled
    }
}