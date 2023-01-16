package com.giussepr.zemoga.domain.usecase

import com.giussepr.zemoga.domain.repository.ZemogaRepository
import javax.inject.Inject
import com.giussepr.zemoga.domain.model.Result
import com.giussepr.zemoga.domain.model.fold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CheckIfPostIsFavoriteUseCase @Inject constructor(private val zemogaRepository: ZemogaRepository) {

  operator fun invoke(postId: Int): Flow<Result<Boolean>> {
    return zemogaRepository.getLocalFavoritePostById(postId).map { result ->
      result.fold(
        onSuccess = { Result.Success(it?.isFavorite ?: false) },
        onFailure = { Result.Error(it) }
      )
    }
  }

}
