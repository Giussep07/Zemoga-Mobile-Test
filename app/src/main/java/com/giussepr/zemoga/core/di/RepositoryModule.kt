package com.giussepr.zemoga.core.di

import android.content.Context
import com.giussepr.zemoga.data.api.PlaceholderApi
import com.giussepr.zemoga.data.database.dao.PostDao
import com.giussepr.zemoga.data.mapper.CommentResponseMapper
import com.giussepr.zemoga.data.mapper.PostDataMapper
import com.giussepr.zemoga.data.mapper.UserResponseMapper
import com.giussepr.zemoga.data.repository.ZemogaRepositoryImpl
import com.giussepr.zemoga.data.repository.datasource.local.ZemogaLocalDataSource
import com.giussepr.zemoga.data.repository.datasource.local.ZemogaLocalDataSourceImpl
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSource
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSourceImpl
import com.giussepr.zemoga.data.utils.NetworkUtils
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  fun provideZemogaRemoteDataSource(placeholderApi: PlaceholderApi): ZemogaRemoteDataSource {
    return ZemogaRemoteDataSourceImpl(placeholderApi)
  }

  @Provides
  fun provideZemogaLocalDataSource(postDao: PostDao): ZemogaLocalDataSource {
    return ZemogaLocalDataSourceImpl(postDao)
  }

  @Provides
  fun providePostResponseMapper(): PostDataMapper = PostDataMapper()

  @Provides
  fun provideUserResponseMapper(): UserResponseMapper = UserResponseMapper()

  @Provides
  fun provideCommentResponseMapper(): CommentResponseMapper = CommentResponseMapper()

  @Provides
  fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils = NetworkUtils(context)

  @Provides
  fun providePostRepository(
    zemogaRemoteDataSource: ZemogaRemoteDataSource,
    zemogaLocalDataSource: ZemogaLocalDataSource,
    postDataMapper: PostDataMapper,
    userResponseMapper: UserResponseMapper,
    commentResponseMapper: CommentResponseMapper,
    networkUtils: NetworkUtils
  ): ZemogaRepository {
    return ZemogaRepositoryImpl(
      zemogaRemoteDataSource,
      zemogaLocalDataSource,
      postDataMapper,
      userResponseMapper,
      commentResponseMapper,
      networkUtils
    )
  }
}
