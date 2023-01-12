package com.giussepr.zemoga.data.repository

import com.giussepr.zemoga.data.mapper.PostResponseMapper
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSource
import com.giussepr.zemoga.domain.model.DomainException
import com.giussepr.zemoga.domain.model.Post
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.giussepr.zemoga.domain.model.Result
import kotlinx.coroutines.flow.flow

class ZemogaRepositoryImpl @Inject constructor(
  private val zemogaRemoteDataSource: ZemogaRemoteDataSource,
  private val postResponseMapper: PostResponseMapper
) : ZemogaRepository {

  override fun getAllPosts(): Flow<Result<List<Post>>> = flow {
    try {
      val response = zemogaRemoteDataSource.getPosts()

      if (response.isSuccessful) {
        response.body()?.let { postResponseList ->
          emit(Result.Success(postResponseList.map { postResponseMapper.mapToPostDomain(it) }))
        } ?: emit(Result.Error(DomainException("Error getting posts")))
      } else {
        emit(
          Result.Error(
            DomainException(
              response.errorBody()?.string() ?: "Something went wrong"
            )
          )
        )
      }
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }
}
