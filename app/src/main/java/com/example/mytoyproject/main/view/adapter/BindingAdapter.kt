package com.example.mytoyproject.main.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("avatarUrl")
fun bindImageFromRes(view: ImageView, string: String?) {
    Glide.with(view.context).load(string).override(1024).into(view)
}