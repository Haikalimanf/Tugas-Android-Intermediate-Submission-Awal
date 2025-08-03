package com.hakif.StoryApp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hakif.StoryApp.data.network.response.story.ListStoryItem
import com.hakif.StoryApp.databinding.ItemListStoryBinding

class StoryAdapter(
    private val onItemClick: (ListStoryItem) -> Unit
): ListAdapter<ListStoryItem,StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemListStoryBinding,
        private val onItemClick: (ListStoryItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            binding.apply {
                tvName.text = storyItem.name
                tvDescription.text = storyItem.description
                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .into(imgUser)
                root.setOnClickListener {
                    onItemClick(storyItem)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }
}