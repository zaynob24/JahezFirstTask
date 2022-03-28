package com.example.jahezfirsttask.presentation.restaurantList

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.databinding.FragmentRestaurantListBinding
import com.example.jahezfirsttask.presentation.restaurantList.RestaurantListAdapter
import com.example.jahezfirsttask.presentation.restaurantList.RestaurantListViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RestaurantListFragment"
@AndroidEntryPoint
class RestaurantListFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantListBinding
    private val restaurantViewModel : RestaurantListViewModel by activityViewModels()

    private lateinit var restaurantListAdapter: RestaurantListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //To make OptionsMenu display in the fragment
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentRestaurantListBinding.inflate(layoutInflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //------------------------------------------------Adapter & Recyclerview------------------------------------------------------------//

        //initiate the adapter and assigned to recyclerview

        restaurantListAdapter = RestaurantListAdapter()
        binding.itemRecyclerview.adapter = restaurantListAdapter

        //------------------------------------------------Get Restaurant List------------------------------------------------------------//

        //call view model to get restaurant List
         restaurantViewModel

        //restaurant List stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                restaurantViewModel.stateFlow.collectLatest {  restaurantListState ->

                    when{

                        restaurantListState.isLoading ->{
                            //show progress Bar
                            binding.progressBar2.visibility = View.VISIBLE
                            Log.d(TAG,"is Loading")
                        }

                        restaurantListState.restaurant.isNotEmpty() ->{
                            //hide progress Bar
                            binding.progressBar2.visibility = View.INVISIBLE
                            Log.d(TAG,restaurantListState.restaurant.toString())
                            // submit the list of restaurants to the recyclerView Adapter
                            restaurantListAdapter.submitList(restaurantListState.restaurant)

                        }

                        restaurantListState.error.isNotBlank() ->{
                            //hide progress Bar
                            binding.progressBar2.visibility =  View.INVISIBLE
                            Log.d(TAG,restaurantListState.error)
                            //show error massage
                            Toast.makeText(requireActivity(), restaurantListState.error, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }

    }
    //------------------------------------------------Options Menu filtering ------------------------------------------------------------//

    //to show OptionsMenu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //filter restaurant based on offer or distance
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.show_all -> {
                Log.d(TAG,"show_all")
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value.restaurant)
                Log.d(TAG,restaurantViewModel.stateFlow.value.restaurant.toString())
                true
            }
            R.id.offers -> {
                Log.d(TAG,"offers")
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value.restaurant.filter { it.hasOffer })
                Log.d(TAG,restaurantViewModel.stateFlow.value.restaurant.filter { it.hasOffer }.toString())
                true
            }
            R.id.distance -> {
                Log.d(TAG,"distance")
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value.restaurant.sortedByDescending { it.distance })
                Log.d(TAG,restaurantViewModel.stateFlow.value.restaurant.sortedByDescending { it.distance }.toString())
                true
            }

            R.id.signOut -> {
                Log.d(TAG,"signOut")
                Log.d(TAG, FirebaseAuth.getInstance().currentUser?.email.toString())
                restaurantViewModel.signOut()
                findNavController().navigate(R.id.action_restaurantListFragment_to_loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}