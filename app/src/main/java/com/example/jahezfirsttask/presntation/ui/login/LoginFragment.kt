package com.example.jahezfirsttask.presntation.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.databinding.FragmentLoginBinding
import com.example.jahezfirsttask.presntation.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var  email: String
    private lateinit var  password: String

    private val loginViewModel : LoginViewModel by activityViewModels()

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        progressDialog = ProgressDialog(requireActivity()).also {
            it.setTitle("Loading...")
            it.setCancelable(false)
        }

        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /////////////////////////////////////////////////////////////////
        //-----------------CHECK LOGIN----------------------------//

        //TODO : erase it
        binding.isloginbutton.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.let {
                // user logged in!
                binding.isLoginTv.text = FirebaseAuth.getInstance().currentUser?.email.toString()

            }?:run {
                // user are not logged in
                binding.isLoginTv.text = "logout"
            }
        }
                        //-----------------LOGOUT----------------------------//
        //TODO : erase it
        //LogOut
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
           // progressDialog.dismiss()
            binding.isLoginTv.text = "logout"
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        binding.loginButton.setOnClickListener {

            takeEntryData() // to collect items data from all fields
            if (checkEntryData()){ // to check if all field contain data and give error massage if not
                //progressDialog.show()

                loginViewModel.login(email, password)
            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.stateFlow.collectLatest {

                    //progressDialog.dismiss()
                    binding.massageTv.text = it.toString()
                }
            }
        }

    }



    //------------------------------------------------------------------------------------------------------------//

    // to collect post data from all fields
    private fun takeEntryData() {

        email = binding.emailLoginTV.text.toString().trim()
        password = binding.passwordLoginTV.text.toString().trim()

    }

    //--------------------------------------------------------------------------------------------------------------//

    // to check if all field contain data and give error massage if not
    private fun checkEntryData() : Boolean {
        var isAllDataFilled = true


        //check email
        if (email.isEmpty() || email.isBlank()) {
            binding.emailLoginTextField.error = getString(R.string.required)
            isAllDataFilled = false

        } else {
            binding.emailLoginTextField.error = null
        }

        //check password
        if (password.isEmpty() || password.isBlank()) {
            binding.passwordLoginTextField.error = getString(R.string.required)
            isAllDataFilled = false
        } else {

            binding.passwordLoginTextField.error = null
        }

        return isAllDataFilled
    }

}



