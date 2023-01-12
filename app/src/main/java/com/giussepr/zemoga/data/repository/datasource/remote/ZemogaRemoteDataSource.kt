package com.giussepr.zemoga.data.repository.datasource.remote

import com.giussepr.zemoga.data.model.PostResponseDTO
import retrofit2.Response

interface ZemogaRemoteDataSource {
  suspend fun getPosts(): Response<List<PostResponseDTO>>
}
