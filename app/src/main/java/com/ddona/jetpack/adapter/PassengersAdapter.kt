package com.ddona.jetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddona.jetpack.databinding.ItemPassengerBinding
import com.ddona.jetpack.model.Passenger

class PassengersAdapter(private val passengers: List<Passenger>) :
    RecyclerView.Adapter<PassengersAdapter.ViewHolder>() {

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
        holder.bind(passengers[position])
    }

    override fun getItemCount(): Int = passengers.size

    class ViewHolder(private val binding: ItemPassengerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(passenger: Passenger) {
            binding.passenger = passenger
            binding.executePendingBindings()
        }

    }
}


