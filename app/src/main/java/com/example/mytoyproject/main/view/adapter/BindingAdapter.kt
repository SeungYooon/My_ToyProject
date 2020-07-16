package com.example.mytoyproject.main.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mytoyproject.R
import com.example.mytoyproject.utils.GlideApp
import com.example.mytoyproject.utils.MyGlide

@BindingAdapter("avatarUrl")
fun bindImageFromRes(view: ImageView, string: String?) {
    GlideApp.with(view.context).load(string).placeholder(R.drawable.ic_search)
        .error(R.mipmap.ic_launcher).override(1024).into(view)
}