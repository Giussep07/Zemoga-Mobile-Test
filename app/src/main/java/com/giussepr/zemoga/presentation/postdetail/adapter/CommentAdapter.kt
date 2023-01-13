package com.giussepr.zemoga.presentation.postdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.giussepr.zemoga.databinding.CommentItemBinding
import com.giussepr.zemoga.domain.model.Comment

class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

  private val commentList: MutableList<Comment> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
    val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CommentViewHolder(binding)
  }

  override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
    holder.bind(commentList[position])
  }

  override fun getItemCount(): Int = commentList.size

  fun setData(CommentList: List<Comment>) {
    this.commentList.clear()
    this.commentList.addAll(CommentList)

    notifyDataSetChanged()
  }

  inner class CommentViewHolder(
    private val binding: CommentItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) {
      binding.tvCommentName.text = comment.name
      binding.tvCommentEmail.text = comment.email
      binding.tvComment.text = comment.body
    }
  }
}
