package com.example.androidproject.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.MainActivity
import com.example.androidproject.databinding.FragmentOverviewBinding
import com.example.androidproject.databinding.GridViewItemBinding
import com.example.androidproject.network.Restaurant

class PhotoGridAdapter(private val onClickListener: OnClickListener) : ListAdapter<Restaurant, PhotoGridAdapter.RestaurantViewHolder>(DiffCallback) {
    class RestaurantViewHolder (private var binding: GridViewItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.property = restaurant
            binding.executePendingBindings()

        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridAdapter.RestaurantViewHolder {
        return RestaurantViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.RestaurantViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(restaurant)
        }
        holder.bind(restaurant)
    }

    class OnClickListener(val clickListener: (restaurant: Restaurant) -> Unit) {
        fun onClick(restaurant: Restaurant) = clickListener(restaurant)
    }
}