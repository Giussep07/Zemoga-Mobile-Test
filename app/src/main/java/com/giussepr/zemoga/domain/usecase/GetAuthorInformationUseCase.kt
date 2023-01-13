package com.giussepr.zemoga.domain.usecase

import com.giussepr.zemoga.domain.repository.ZemogaRepository
import javax.inject.Inject

class GetAuthorInformationUseCase @Inject constructor(
  private val zemogaRepository: ZemogaRepository
) {

  operator fun invoke(userId: Int)= zemogaRepository.getUserInformation(userId)

}
