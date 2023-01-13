package com.giussepr.zemoga.data.mapper

import com.giussepr.zemoga.data.model.UserResponseDTO
import com.giussepr.zemoga.domain.model.User

class UserResponseMapper {

  fun mapToDomainUser(userResponse: UserResponseDTO) = User(
    id = userResponse.id,
    name = userResponse.name,
    username = userResponse.username,
    email = userResponse.email,
    phone = userResponse.phone,
    website = userResponse.website
  )
}
