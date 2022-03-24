package com.example.jahezfirsttask.presentation.ui.restaurant_list

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
import com.example.jahezfirsttask.databinding.FragmentRestaurantListBinding
import com.example.jahezfirsttask.presentation.restaurant_list.RestaurantListAdapter
import com.example.jahezfirsttask.presentation.restaurant_list.RestaurantListViewModel
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

}