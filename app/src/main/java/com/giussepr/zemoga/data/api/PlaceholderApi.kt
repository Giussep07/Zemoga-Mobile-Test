package com.giussepr.zemoga.data.api

import com.giussepr.zemoga.data.model.PostResponseDTO
import retrofit2.Response
import retrofit2.http.GET

interface PlaceholderApi {

  @GET("posts")
  suspend fun getPosts(): Response<List<PostResponseDTO>>
}
