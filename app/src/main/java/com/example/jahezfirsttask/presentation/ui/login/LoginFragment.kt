package com.example.jahezfirsttask.presentation.ui.login

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
import com.example.jahezfirsttask.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
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

        //------------------------------------------------nave to Register Fragment ------------------------------------------------------------//

         //to open Register Fragment
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }

        //------------------------------------------------check user authentication ------------------------------------------------------------//

        // check if user already loggedIn go to homepage(restaurantListFragment)
         if(loginViewModel.isUserAuthenticated())
         {
             findNavController().navigate(R.id.action_loginFragment_to_restaurantListFragment)
         }

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
            binding.isLoginTv.text = "logout"
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //------------------------------------------------login------------------------------------------------------------//

        //Login
        binding.loginButton.setOnClickListener {

            collectDataFromUser() // to collect items data from all fields
            if (checkDataValidity()){ // to check if all field contain data and give error massage if not

                //call firebase login
                loginViewModel.login(email, password)

            }else{
                Toast.makeText(requireContext(),getText(R.string.fill_required), Toast.LENGTH_SHORT).show()
            }
        }


        //Login stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.stateFlow.collectLatest { loginState ->

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


    //------------------------------------------------collectData------------------------------------------------------------//

    // to collect post data from all fields
    private fun collectDataFromUser() {

        email = binding.emailLoginTV.text.toString().trim()
        password = binding.passwordLoginTV.text.toString().trim()

    }

    //--------------------------------------------------check Data Validity------------------------------------------------------------//

    // to check if all field contain data and give error massage if not
    private fun checkDataValidity() : Boolean {
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



