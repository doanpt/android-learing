package com.ddona.jetpack.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ddona.jetpack.databinding.ItemPassengerBinding
import com.ddona.jetpack.model.Passenger

class PassengersAdapter :
    PagingDataAdapter<Passenger, PassengersAdapter.ViewHolder>(PassengersComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPassengerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        Log.d("doanpt", "onBindViewHolder $item")
        item?.let { holder.bind(it) }
    }

    class ViewHolder(private val binding: ItemPassengerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(passenger: Passenger) {
            binding.passenger = passenger
            binding.executePendingBindings()
        }

    }

    object PassengersComparator : DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem == newItem
        }
    }
}


