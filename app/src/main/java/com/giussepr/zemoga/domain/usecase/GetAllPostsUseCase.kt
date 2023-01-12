package com.giussepr.zemoga.domain.usecase

import com.giussepr.zemoga.domain.mapper.PostMapper
import com.giussepr.zemoga.domain.model.fold
import com.giussepr.zemoga.domain.repository.ZemogaRepository
import com.giussepr.zemoga.presentation.model.UiPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.giussepr.zemoga.domain.model.Result
import kotlinx.coroutines.flow.collect

class GetAllPostsUseCase @Inject constructor(
  private val zemogaRepository: ZemogaRepository,
  private val postMapper: PostMapper
) {

  operator fun invoke(): Flow<Result<List<UiPost>>> = flow {
    zemogaRepository.getAllPosts().map { result ->
      result.fold(
        onSuccess = { postList ->
          emit(Result.Success(postList.map { postMapper.mapToUiPost(it) }))
        },
        onFailure = {
          emit(Result.Error(it))
        }
      )
    }.collect()
  }

}
