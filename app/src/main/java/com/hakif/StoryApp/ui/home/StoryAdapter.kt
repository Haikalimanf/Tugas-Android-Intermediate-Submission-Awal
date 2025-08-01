package com.hakif.StoryApp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hakif.StoryApp.data.network.response.story.ListStoryItem
import com.hakif.StoryApp.databinding.ItemListStoryBinding

class StoryAdapter(
    val story : MutableList<ListStoryItem>,
    private val onItemClick: (ListStoryItem) -> Unit
): RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return story.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(story[position])
    }
}