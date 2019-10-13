package com.example.android.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class PhotoListAdapter : ListAdapter<MarsProperty, PhotoHolder>(MarsPropertyDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        return holder.bind(getItem(position))
    }

}

class MarsPropertyDiffUtil : DiffUtil.ItemCallback<MarsProperty>() {
    override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem == newItem
    }

}

class PhotoHolder(private val binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): PhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = GridViewItemBinding.inflate(layoutInflater)
            return PhotoHolder(binding)
        }
    }

    fun bind(property: MarsProperty) {
        binding.property = property
        binding.executePendingBindings()
    }

}

