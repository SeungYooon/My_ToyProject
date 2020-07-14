package com.example.mytoyproject.main.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mytoyproject.R

@BindingAdapter("avatarUrl")
fun bindImageFromRes(view: ImageView, string: String?) {
    Glide.with(view.context).load(string).error(R.mipmap.ic_launcher).override(1024).into(view)
}