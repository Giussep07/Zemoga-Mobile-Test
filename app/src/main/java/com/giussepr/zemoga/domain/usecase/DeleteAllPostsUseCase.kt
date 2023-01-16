package com.giussepr.zemoga.domain.usecase

import com.giussepr.zemoga.domain.repository.ZemogaRepository
import javax.inject.Inject

class DeleteAllPostsUseCase @Inject constructor(private val zemogaRepository: ZemogaRepository) {

  operator fun invoke() = zemogaRepository.deleteAllPosts()
}

