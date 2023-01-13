package com.giussepr.zemoga.data.repository.datasource.remote

import com.giussepr.zemoga.data.api.PlaceholderApi
import com.giussepr.zemoga.data.model.CommentResponseDTO
import com.giussepr.zemoga.data.model.PostResponseDTO
import com.giussepr.zemoga.data.model.UserResponseDTO
import retrofit2.Response
import javax.inject.Inject

class ZemogaRemoteDataSourceImpl @Inject constructor(private val placeholderApi: PlaceholderApi) :
  ZemogaRemoteDataSource {

  override suspend fun getPosts(): Response<List<PostResponseDTO>> {
    return placeholderApi.getPosts()
  }

  override suspend fun getUserInfo(userId: Int): Response<List<UserResponseDTO>> {
    return placeholderApi.getUserInfo(userId)
  }

  override suspend fun getCommentsByPostId(postId: Int): Response<List<CommentResponseDTO>> {
    return placeholderApi.getCommentsByPostId(postId)
  }
}
