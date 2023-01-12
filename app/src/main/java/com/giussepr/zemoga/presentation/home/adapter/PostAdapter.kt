package com.giussepr.zemoga.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giussepr.zemoga.databinding.PostItemBinding
import com.giussepr.zemoga.domain.model.Post

class PostAdapter(): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

  private val postList: MutableList<Post> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bind(postList[position])
  }

  override fun getItemCount(): Int = postList.size

  fun setData(postList: List<Post>) {
    this.postList.clear()
    this.postList.addAll(postList)

    notifyDataSetChanged()
  }

  inner class PostViewHolder(private val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
      binding.tvPostTitle.text = post.title
      binding.tvPostBody.text = post.body
    }
  }
}
