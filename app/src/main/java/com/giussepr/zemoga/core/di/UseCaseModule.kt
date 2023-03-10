package com.giussepr.zemoga.core.di

import com.giussepr.zemoga.domain.mapper.PostMapper
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import com.giussepr.zemoga.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

  @Provides
  fun providePostMapper(): PostMapper = PostMapper()

  @Provides
  fun provideGetPostsUseCase(zemogaRepository: ZemogaRepository, postMapper: PostMapper) =
    GetAllPostsUseCase(zemogaRepository, postMapper)

  @Provides
  fun provideGetUserInformationUseCase(zemogaRepository: ZemogaRepository) =
    GetAuthorInformationUseCase(zemogaRepository)

  @Provides
  fun provideGetCommentsByPostIdUseCase(zemogaRepository: ZemogaRepository) =
    GetCommentsByPostIdUseCase(zemogaRepository)

  @Provides
  fun provideCheckIfPostIsFavoriteUseCase(zemogaRepository: ZemogaRepository) =
    CheckIfPostIsFavoriteUseCase(zemogaRepository)

  @Provides
  fun provideSetPostAsFavoriteUseCase(zemogaRepository: ZemogaRepository) =
    SetPostAsFavoriteUseCase(zemogaRepository)
}
