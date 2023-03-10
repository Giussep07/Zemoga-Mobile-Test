package com.giussepr.zemoga.data.repository

import com.giussepr.zemoga.data.database.entity.UserEntity
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

  override fun getAllPosts(forceRefresh: Boolean): Flow<Result<List<Post>>> = flow {
    try {

      // Get all posts from local database
      val lastUpdate: Long = zemogaLocalDataSource.getAllPosts().first().lastOrNull()?.createdAt ?: 0L

      // Check if the cache is valid
      val cacheIsExpired = cacheIsExpired(lastUpdate)

      if (networkUtils.isInternetAvailable() && cacheIsExpired || forceRefresh) {
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

      if (networkUtils.isInternetAvailable()) {

        val response = zemogaRemoteDataSource.getUserInfo(userId)

        if (response.isSuccessful) {
          response.body()?.let { userResponse ->
            if (userResponse.isNotEmpty()) {
              zemogaLocalDataSource.saveUser(userResponseMapper.mapResponseUserToEntity(userResponse.first()))
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
      } else {
        // Get the user from the local database
        val user: UserEntity? = zemogaLocalDataSource.getUserById(userId)
        if (user != null) {
          emit(Result.Success(userResponseMapper.mapEntityToDomainUser(user)))
        } else {
          emit(Result.Error(DomainException("user information not found")))
        }
      }
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun getCommentsByPostId(postId: Int): Flow<Result<List<Comment>>> = flow {
    try {
      if (networkUtils.isInternetAvailable()) {
        val response = zemogaRemoteDataSource.getCommentsByPostId(postId)

        if (response.isSuccessful) {
          response.body()?.let { commentsResponse ->
            zemogaLocalDataSource.saveComments(commentsResponse.map { commentResponseMapper.mapResponseToEntity(it) })

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
      } else {
        // Get the comments from the local database
        val comments: List<Comment> = zemogaLocalDataSource.getCommentsByPostId(postId)
          .map { commentResponseMapper.mapEntityToDomainComment(it) }

        emit(Result.Success(comments))
      }
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun getLocalFavoritePostById(postId: Int): Flow<Result<Post?>> = flow {
    try {
      zemogaLocalDataSource.getFavoritePostById(postId)?.let { postEntity ->
        emit(Result.Success(postDataMapper.mapEntityToPostDomain(postEntity)))
      } ?: emit(Result.Success(null))
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun setPostAsFavorite(postId: Int, isFavorite: Boolean): Flow<Result<Boolean>> = flow {
    try {
      zemogaLocalDataSource.setPostAsFavorite(postId, isFavorite)
      emit(Result.Success(true))
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun deletePost(postId: Int): Flow<Result<Boolean>> = flow {
    try {
      zemogaLocalDataSource.deletePost(postId)
      emit(Result.Success(true))
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun deleteAllPosts(): Flow<Result<Boolean>> = flow {
    try {
      zemogaLocalDataSource.deleteAllExceptFavorites()
      emit(Result.Success(true))
    } catch (e: Exception) {
      emit(Result.Error(DomainException(e.message ?: "Something went wrong")))
    }
  }

  override fun getLocalPosts(): Flow<Result<List<Post>>> = flow {
    try {
      emit(
        Result.Success(zemogaLocalDataSource.getAllPosts().first().map { postDataMapper.mapEntityToPostDomain(it) })
      )
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
    private val DEFAULT_REFRESH_RATE_MS = TimeUnit.DAYS.toMillis(1)
  }
}
