package com.giussepr.zemoga.data.repository.datasource.local

import com.giussepr.zemoga.data.database.entity.CommentEntity
import com.giussepr.zemoga.data.database.entity.PostEntity
import com.giussepr.zemoga.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ZemogaLocalDataSource {
  fun getAllPosts(): Flow<List<PostEntity>>
  suspend fun insert(post: PostEntity)
  suspend fun insertAll(postList: List<PostEntity>)
  suspend fun deleteAll()
  suspend fun getFavoritePostById(postId: Int): PostEntity?
  suspend fun setPostAsFavorite(postId: Int, isFavorite: Boolean)
  suspend fun deletePost(postId: Int)
  suspend fun deleteAllExceptFavorites()
  suspend fun saveUser(userEntity: UserEntity)
  suspend fun getUserById(userId: Int): UserEntity?
  suspend fun saveComments(commentList: List<CommentEntity>)
}
