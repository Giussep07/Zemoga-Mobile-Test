package com.giussepr.zemoga.data.api

import com.giussepr.zemoga.data.model.CommentResponseDTO
import com.giussepr.zemoga.data.model.PostResponseDTO
import com.giussepr.zemoga.data.model.UserResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceholderApi {

  @GET("posts")
  suspend fun getPosts(): Response<List<PostResponseDTO>>

  @GET("users")
  suspend fun getUserInfo(@Query("id") userId: Int): Response<List<UserResponseDTO>>

  @GET("comments")
  suspend fun getCommentsByPostId(@Query("postId") postId: Int): Response<List<CommentResponseDTO>>
}
