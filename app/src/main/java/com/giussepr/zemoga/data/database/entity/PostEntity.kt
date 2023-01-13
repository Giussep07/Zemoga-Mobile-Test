package com.giussepr.zemoga.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class PostEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  @ColumnInfo(name = "post_id")
  val postId: Int,
  @ColumnInfo(name = "user_id")
  val userId: Int,
  @ColumnInfo(name = "title")
  val title: String,
  @ColumnInfo(name = "body")
  val body: String,
  @ColumnInfo(name = "is_favorite")
  val isFavorite: Boolean = false
)
