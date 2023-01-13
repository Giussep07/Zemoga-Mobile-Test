package com.giussepr.zemoga.domain.repository

import com.giussepr.zemoga.domain.model.Post
import kotlinx.coroutines.flow.Flow
import com.giussepr.zemoga.domain.model.Result
import com.giussepr.zemoga.domain.model.User

interface ZemogaRepository {
  fun getAllPosts(): Flow<Result<List<Post>>>
  fun getUserInformation(userId: Int): Flow<Result<User>>
}
