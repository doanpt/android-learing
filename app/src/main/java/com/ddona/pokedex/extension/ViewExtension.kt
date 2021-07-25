package com.ddona.pokedex.extension

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ddona.pokedex.R

@BindingAdapter("set_url")
fun bindImage(img: ImageView, link: String) {
    Log.d("doanpt","load url: $link")
    Glide.with(img).load(link).placeholder(R.drawable.ic_pokemon).error(R.drawable.pokemon).into(img)
}