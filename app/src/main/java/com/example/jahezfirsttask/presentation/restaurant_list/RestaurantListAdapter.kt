package com.example.jahezfirsttask.presentation.restaurant_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jahezfirsttask.JahezApplication
import com.example.jahezfirsttask.R
import com.example.jahezfirsttask.databinding.ItemRestaurantBinding
import com.example.jahezfirsttask.domain.model.Restaurant

class RestaurantListAdapter : ListAdapter<Restaurant, RestaurantListAdapter.ItemViewHolder>(diff) {


    class ItemViewHolder(private val binding: ItemRestaurantBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurant) {
            binding.item = item

            // TO show restaurant image
            Glide.with(JahezApplication.appContext).load(item.image)
                .placeholder(R.drawable.pot)
                .into(binding.itemImageView)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemRestaurantBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // using DiffUtil will keep old list data and just update by add the new one

    companion object {
        val diff by lazy {
            object : DiffUtil.ItemCallback<Restaurant?>() {
                override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}