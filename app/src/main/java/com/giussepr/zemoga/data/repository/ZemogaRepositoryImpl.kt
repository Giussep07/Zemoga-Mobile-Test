package com.giussepr.zemoga.data.repository

import com.giussepr.zemoga.data.mapper.CommentResponseMapper
import com.giussepr.zemoga.data.mapper.PostDataMapper
import com.giussepr.zemoga.data.mapper.UserResponseMapper
import com.giussepr.zemoga.data.repository.datasource.local.ZemogaLocalDataSource
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSource
import com.giussepr.zemoga.data.utils.NetworkUtils
import com.giussepr.zemoga.domain.model.*
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ZemogaRepositoryImpl @Inject constructor(
  private val zemogaRemoteDataSource: ZemogaRemoteDataSource,
  private val zemogaLocalDataSource: ZemogaLocalDataSource,
  private val postDataMapper: PostDataMapper,
  private val userResponseMapper: UserResponseMapper,
  private val commentResponseMapper: CommentResponseMapper,
  private val networkUtils: NetworkUtils,
) : ZemogaRepository {

  override fun getAllPosts(): Flow<Result<List<Post>>> = flow {
    try {

      // Get all posts from local database
      val localPosts = zemogaLocalDataSource.getAllPosts().first()

      // Check if the cache is valid
      val cacheIsExpired = cacheIsExpired(localPosts.last().createdAt)

      if (networkUtils.isInternetAvailable() && cacheIsExpired) {
        val response = zemogaRemoteDataSource.getPosts()

        if (response.isSuccessful) {
          response.body()?.let { postResponseList ->

            // Delete all the posts from the local database
            zemogaLocalDataSource.deleteAll()
            // Insert the posts from the remote data source into the local database
            zemogaLocalDataSource.insertAll(
              postResponseList.map { postDataMapper.mapResponseToPostEntity(it) }
            )
          }
        }
      }

      // Get the posts from the local database
      emit(
        Result.Success(
          zemogaLocalDataSource.getAllPosts().first()
            .map { postDataMapper.mapEntityToPostDomain(it) })
      )

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

  private fun cacheIsExpired(lastUpdate: Long): Boolean {
    val currentTime = System.currentTimeMillis()
    val cacheTime = currentTime - lastUpdate

    return cacheTime > DEFAULT_REFRESH_RATE_MS
  }

  companion object {
    private val DEFAULT_REFRESH_RATE_MS = TimeUnit.MINUTES.toMillis(10)
  }
}
