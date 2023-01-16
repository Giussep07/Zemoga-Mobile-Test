package com.giussepr.zemoga.core.di

import com.giussepr.zemoga.domain.usecase.*
import com.giussepr.zemoga.presentation.postdetail.PostDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

  @Provides
  fun providePostDetailViewModel(
    getAuthorInformationUseCase: GetAuthorInformationUseCase,
    getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase,
    checkIfPostIsFavoriteUseCase: CheckIfPostIsFavoriteUseCase,
    setPostAsFavoriteUseCase: SetPostAsFavoriteUseCase,
    deletePostUseCase: DeletePostUseCase,
  ) = PostDetailViewModel(
    getAuthorInformationUseCase,
    getCommentsByPostIdUseCase,
    checkIfPostIsFavoriteUseCase,
    setPostAsFavoriteUseCase,
    deletePostUseCase,
    MutableStateFlow(false)
  )
}
