package com.giussepr.zemoga.data.repository

import com.giussepr.zemoga.data.mapper.CommentResponseMapper
import com.giussepr.zemoga.data.mapper.PostResponseMapper
import com.giussepr.zemoga.data.mapper.UserResponseMapper
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSource
import com.giussepr.zemoga.domain.model.*
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class ZemogaRepositoryImpl @Inject constructor(
  private val zemogaRemoteDataSource: ZemogaRemoteDataSource,
  private val postResponseMapper: PostResponseMapper,
  private val userResponseMapper: UserResponseMapper,
  private val commentResponseMapper: CommentResponseMapper
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

  override fun getUserInformation(userId: Int): Flow<Result<User>> = flow {
    try {
      val response = zemogaRemoteDataSource.getUserInfo(userId)

      if (response.isSuccessful) {
        response.body()?.let { userResponse ->
          if (userResponse.isNotEmpty()) {
            emit(Result.Success(userResponseMapper.mapToDomainUser(userResponse.first())))
          } else {
            emit(Result.Error(DomainException("user information not found")))
          }
        } ?: emit(Result.Error(DomainException("Error getting user information")))
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

  override fun getCommentsByPostId(postId: Int): Flow<Result<List<Comment>>> = flow {
    try {
      val response = zemogaRemoteDataSource.getCommentsByPostId(postId)

      if (response.isSuccessful) {
        response.body()?.let { commentsResponse ->
          emit(Result.Success(commentsResponse.map { commentResponseMapper.mapToDomainComment(it) }))
        } ?: emit(Result.Error(DomainException("Error getting comments")))
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
