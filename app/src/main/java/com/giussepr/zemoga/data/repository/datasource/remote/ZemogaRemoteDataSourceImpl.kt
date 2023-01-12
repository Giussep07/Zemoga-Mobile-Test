package com.giussepr.zemoga.data.repository.datasource.remote

import com.giussepr.zemoga.data.api.PlaceholderApi
import com.giussepr.zemoga.data.model.PostResponseDTO
import retrofit2.Response
import javax.inject.Inject

class ZemogaRemoteDataSourceImpl @Inject constructor(private val placeholderApi: PlaceholderApi) :
  ZemogaRemoteDataSource {

  override suspend fun getPosts(): Response<List<PostResponseDTO>> {
    return placeholderApi.getPosts()
  }
}
