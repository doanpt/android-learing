package com.ddona.jetpack.extension

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ddona.jetpack.R
import com.ddona.jetpack.model.Passenger

@BindingAdapter("set_url")
fun bindImage(img: ImageView, link: String) {
    Glide.with(img).load(link)
        .placeholder(R.drawable.ic_pokemon)
        .error(R.drawable.pokemon)
        .into(img)
}

@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("text_html")
fun setText(tv: TextView, data: String) {
    tv.text =
        Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
}

@SuppressLint("SetTextI18n")
@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("name_trip")
fun setNameTrip(tv: TextView, passenger: Passenger) {
    tv.text = "${passenger.name}, ${passenger.trips} Trips"
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

