package com.giussepr.zemoga.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giussepr.zemoga.data.database.entity.CommentEntity

@Dao
interface CommentDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(comments: List<CommentEntity>)

  @Query("SELECT * FROM comment WHERE postId = :postId")
  fun getCommentsByPostId(postId: Int): List<CommentEntity>

}
