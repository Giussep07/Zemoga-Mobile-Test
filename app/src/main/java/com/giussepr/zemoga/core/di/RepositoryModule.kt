package com.giussepr.zemoga.core.di

import com.giussepr.zemoga.data.api.PlaceholderApi
import com.giussepr.zemoga.data.mapper.PostResponseMapper
import com.giussepr.zemoga.data.repository.ZemogaRepositoryImpl
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSource
import com.giussepr.zemoga.data.repository.datasource.remote.ZemogaRemoteDataSourceImpl
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  fun provideZemogaRemoteDataSource(placeholderApi: PlaceholderApi): ZemogaRemoteDataSource {
    return ZemogaRemoteDataSourceImpl(placeholderApi)
  }

  @Provides
  fun providePostResponseMapper(): PostResponseMapper = PostResponseMapper()

  @Provides
  fun providePostRepository(
    zemogaRemoteDataSource: ZemogaRemoteDataSource,
    postResponseMapper: PostResponseMapper
  ): ZemogaRepository {
    return ZemogaRepositoryImpl(zemogaRemoteDataSource, postResponseMapper)
  }
}
