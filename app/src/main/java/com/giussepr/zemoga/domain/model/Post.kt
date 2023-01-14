package com.giussepr.zemoga.domain.model

data class Post(
  val userId: Int,
  val id: Int,
  val title: String,
  val body: String,
  val isFavorite: Boolean
)
