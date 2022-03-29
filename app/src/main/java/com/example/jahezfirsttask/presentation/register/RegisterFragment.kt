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

        initViewModelDataBinding()
        clickListener()
        initCollectFlow()

    }

    private fun initViewModelDataBinding() {

        binding.viewModel = registerViewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = this   }

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

        //Register
        binding.signUpButton.setOnClickListener {

            collectDataFromUser() // to collect items data from all fields
            if (registerViewModel.checkDataValidity(email,password,confirmPassword)){ // to check if all field contain data and give error massage if not

                //call firebase register
                registerViewModel.Register(email, password)

            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
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