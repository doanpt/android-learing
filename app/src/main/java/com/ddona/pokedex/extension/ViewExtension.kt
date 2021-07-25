package com.ddona.pokedex.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


class ViewExtension {
    @BindingAdapter("set_url")
    fun bindImage(img: ImageView, link: String?) {
        Glide.with(img).load(link).into(img)
    }
}