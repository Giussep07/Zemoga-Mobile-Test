package com.giussepr.zemoga.domain.usecase

import com.giussepr.zemoga.domain.repository.ZemogaRepository
import javax.inject.Inject

class GetCommentsByPostIdUseCase @Inject constructor(private val zemogaRepository: ZemogaRepository) {

  operator fun invoke(postId: Int) = zemogaRepository.getCommentsByPostId(postId)

}
