package com.giussepr.zemoga.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.giussepr.zemoga.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

  @Query("SELECT * FROM post")
  fun getAll(): Flow<List<PostEntity>>

  @Insert
  suspend fun insert(post: PostEntity)

  @Query("DELETE FROM post")
  suspend fun deleteAll()
}
