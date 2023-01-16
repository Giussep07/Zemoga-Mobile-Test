package com.giussepr.zemoga.data.repository.datasource.local

import com.giussepr.zemoga.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

interface ZemogaLocalDataSource {
  fun getAllPosts(): Flow<List<PostEntity>>
  suspend fun insert(post: PostEntity)
  suspend fun insertAll(postList: List<PostEntity>)
  suspend fun deleteAll()
  suspend fun getFavoritePostById(postId: Int): PostEntity?
  suspend fun setPostAsFavorite(postId: Int, isFavorite: Boolean)
}
