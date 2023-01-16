package com.giussepr.zemoga.data.repository.datasource.local

import com.giussepr.zemoga.data.database.dao.CommentDao
import com.giussepr.zemoga.data.database.dao.PostDao
import com.giussepr.zemoga.data.database.dao.UserDao
import com.giussepr.zemoga.data.database.entity.CommentEntity
import com.giussepr.zemoga.data.database.entity.PostEntity
import com.giussepr.zemoga.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ZemogaLocalDataSourceImpl @Inject constructor(
  private val postDao: PostDao,
  private val userDao: UserDao,
  private val commentDao: CommentDao
) : ZemogaLocalDataSource {

  override fun getAllPosts(): Flow<List<PostEntity>> {
    return postDao.getAll()
  }

  override suspend fun insert(post: PostEntity) {
    postDao.insert(post)
  }

  override suspend fun insertAll(postList: List<PostEntity>) {
    postDao.insertAll(postList)
  }

  override suspend fun deleteAll() {
    postDao.deleteAll()
  }

  override suspend fun getFavoritePostById(postId: Int): PostEntity? {
    return postDao.getFavoritePostById(postId)
  }

  override suspend fun setPostAsFavorite(postId: Int, isFavorite: Boolean) {
    postDao.setPostAsFavorite(postId, isFavorite)
  }

  override suspend fun deletePost(postId: Int) {
    postDao.deletePost(postId)
  }

  override suspend fun deleteAllExceptFavorites() {
    postDao.deleteAllExceptFavorites()
  }

  override suspend fun saveUser(userEntity: UserEntity) {
    userDao.insert(userEntity)
  }

  override suspend fun getUserById(userId: Int): UserEntity? {
    return userDao.getUserById(userId)
  }

  override suspend fun saveComments(commentList: List<CommentEntity>) {
    commentDao.insertAll(commentList)
  }

  override fun getCommentsByPostId(postId: Int): List<CommentEntity> {
    return commentDao.getCommentsByPostId(postId)
  }
}
