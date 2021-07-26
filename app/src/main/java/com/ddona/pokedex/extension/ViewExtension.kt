package com.ddona.pokedex.extension

import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ddona.pokedex.R

@BindingAdapter("set_url")
fun bindImage(img: ImageView, link: String) {
    Log.d("doanpt", "load url: $link")
    Glide.with(img).load(link).placeholder(R.drawable.ic_pokemon).error(R.drawable.pokemon)
        .into(img)
}

@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("text_html")
fun setText(tv: TextView, data: String) {
    tv.text =
        Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
}