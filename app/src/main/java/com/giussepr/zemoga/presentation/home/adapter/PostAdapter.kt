package com.giussepr.zemoga.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giussepr.zemoga.R
import com.giussepr.zemoga.databinding.PostItemBinding
import com.giussepr.zemoga.presentation.model.UiPost

typealias OnPostClickListener = (UiPost) -> Unit

class PostAdapter(
  private val context: Context,
  private val onPostClickListener: OnPostClickListener
) :
  RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

  private val postList: MutableList<UiPost> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding, onPostClickListener)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bind(postList[position])
  }

  override fun getItemCount(): Int = postList.size

  fun setData(postList: List<UiPost>) {
    this.postList.clear()
    this.postList.addAll(postList)

    notifyDataSetChanged()
  }

  inner class PostViewHolder(
    private val binding: PostItemBinding,
    private val onPostClickListener: OnPostClickListener
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: UiPost) {
      binding.tvPostTitle.text = post.title
      binding.tvPostBody.text = post.body

      if (post.isFavorite) {
        binding.ivFavorite.setImageDrawable(
          context.resources.getDrawable(
            R.drawable.ic_favorite_filled,
            context.theme
          )
        )
      } else {
        binding.ivFavorite.setImageDrawable(
          context.resources.getDrawable(
            R.drawable.ic_favorite_border,
            context.theme
          )
        )
      }

      binding.root.setOnClickListener { onPostClickListener(post) }
    }
  }
}
