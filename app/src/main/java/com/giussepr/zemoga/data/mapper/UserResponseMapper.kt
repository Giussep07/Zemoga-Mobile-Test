package com.giussepr.zemoga.data.mapper

import com.giussepr.zemoga.data.database.entity.UserEntity
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

  fun mapResponseUserToEntity(userResponse: UserResponseDTO) = UserEntity(
    id = 0,
    userId = userResponse.id,
    name = userResponse.name,
    username = userResponse.username,
    email = userResponse.email,
    phone = userResponse.phone,
    website = userResponse.website
  )

  fun mapEntityToDomainUser(userEntity: UserEntity) = User(
    id = userEntity.userId,
    name = userEntity.name,
    username = userEntity.username,
    email = userEntity.email,
    phone = userEntity.phone,
    website = userEntity.website
  )
}
