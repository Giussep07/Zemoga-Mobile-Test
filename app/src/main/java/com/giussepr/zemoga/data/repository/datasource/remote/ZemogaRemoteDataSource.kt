package com.giussepr.zemoga.data.repository.datasource.remote

import com.giussepr.zemoga.data.model.CommentResponseDTO
import com.giussepr.zemoga.data.model.PostResponseDTO
import com.giussepr.zemoga.data.model.UserResponseDTO
import retrofit2.Response

interface ZemogaRemoteDataSource {
  suspend fun getPosts(): Response<List<PostResponseDTO>>
  suspend fun getUserInfo(userId: Int): Response<List<UserResponseDTO>>
  suspend fun getCommentsByPostId(postId: Int): Response<List<CommentResponseDTO>>
}
