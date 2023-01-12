package com.giussepr.zemoga.core.di

import com.giussepr.zemoga.domain.repository.ZemogaRepository
import com.giussepr.zemoga.domain.usecase.GetAllPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

  @Provides
  fun provideGetPostsUseCase(zemogaRepository: ZemogaRepository) =
    GetAllPostsUseCase(zemogaRepository)
}
