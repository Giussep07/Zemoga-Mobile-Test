package com.giussepr.zemoga.data.model

data class CommentResponseDTO(
  val postId: Int,
  val id: Int,
  val name: String,
  val email: String,
  val body: String
)
