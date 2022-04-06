package com.example.jahezfirsttask.presentation.restaurantList

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.base.BaseFragment
import com.example.jahezfirsttask.databinding.FragmentRestaurantListBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "RestaurantListFragment"
@AndroidEntryPoint
class RestaurantListFragment : BaseFragment() {

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
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get Restaurant List
        initCollectFlow()
    }

    private fun init() {

        //assign this viewModel to base viewModel
        setBaseViewModel(restaurantViewModel)
        setUIState()

        //initiate the adapter and assigned to recyclerview
        restaurantListAdapter = RestaurantListAdapter()
        binding.itemRecyclerview.adapter = restaurantListAdapter

        restaurantViewModel.getRestaurant()

    }

    //Get Restaurant List by Collect Flow
    private fun initCollectFlow() {

        //restaurant List stateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                restaurantViewModel.stateFlow.collectLatest {  restaurantList ->

                  if(restaurantList.isNotEmpty()){
                          Log.d(TAG,restaurantList.toString())
                          // submit the list of restaurants to the recyclerView Adapter
                          restaurantListAdapter.submitList(restaurantList) } } }
            }
        }

    //-----Options Menu filtering

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
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value)
                Log.d(TAG,restaurantViewModel.stateFlow.value.toString())
                true
            }
            R.id.offers -> {
                Log.d(TAG,"offers")
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value.filter { it.hasOffer })
                Log.d(TAG,restaurantViewModel.stateFlow.value.filter { it.hasOffer }.toString())
                true
            }
            R.id.distance -> {
                Log.d(TAG,"distance")
                restaurantListAdapter.submitList(restaurantViewModel.stateFlow.value.sortedByDescending { it.distance })
                Log.d(TAG,restaurantViewModel.stateFlow.value.sortedByDescending { it.distance }.toString())
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