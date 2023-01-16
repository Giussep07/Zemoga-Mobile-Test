package com.giussepr.zemoga.data.database.dao

import androidx.room.*
import com.giussepr.zemoga.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

  @Query("SELECT * FROM post order by is_favorite desc")
  fun getAll(): Flow<List<PostEntity>>

  @Insert
  suspend fun insert(post: PostEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(postList: List<PostEntity>)

  @Query("DELETE FROM post")
  suspend fun deleteAll()

  @Query("SELECT * FROM post WHERE post_id = :postId")
  suspend fun getFavoritePostById(postId: Int): PostEntity?

  @Query("UPDATE post SET is_favorite = :isFavorite WHERE post_id = :postId")
  suspend fun setPostAsFavorite(postId: Int, isFavorite: Boolean)
}
