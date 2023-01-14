package com.giussepr.zemoga.data.repository.datasource.local

import com.giussepr.zemoga.data.database.dao.PostDao
import com.giussepr.zemoga.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ZemogaLocalDataSourceImpl @Inject constructor(private val postDao: PostDao) :
  ZemogaLocalDataSource {

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
}
