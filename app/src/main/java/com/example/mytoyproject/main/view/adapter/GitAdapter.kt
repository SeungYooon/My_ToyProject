package com.example.mytoyproject.main.view.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytoyproject.main.view.DetailActivity
import com.example.mytoyproject.R
import com.example.mytoyproject.databinding.ListItemBinding
import com.example.mytoyproject.network.model.Item

class GitAdapter(private val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<GitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val listener = View.OnClickListener {
            Toast.makeText(it.context, "move to ${item.repositoryName}", Toast.LENGTH_SHORT).show()

            holder.itemView.setBackgroundColor(Color.LTGRAY)

            android.os.Handler().postDelayed({
                holder.itemView.setBackgroundColor(Color.WHITE)
            }, 2000)

            val intent = Intent(it.context, DetailActivity::class.java)

            intent.putExtra(DetailActivity.AVATAR_URL, item.owner.avatarUrl)
            intent.putExtra(DetailActivity.OWNER, item.owner.ownerName)
            intent.putExtra(DetailActivity.REPO, item.repositoryName)

            it.context.startActivity(intent)
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: View.OnClickListener, item: Item) {
            binding.apply {

                gitItem = item

                itemView.setOnClickListener(listener)
            }
        }
    }

    fun updateRepo(itemList: List<Item>) {
        this.itemList.apply {
            clear()
            addAll(itemList)
            notifyDataSetChanged()
        }
    }
}