package com.giussepr.zemoga.data.mapper

import com.giussepr.zemoga.data.model.PostResponseDTO
import com.giussepr.zemoga.domain.model.Post

class PostResponseMapper {

  fun mapToPostDomain(postResponseDTO: PostResponseDTO): Post {
    return Post(
      userId = postResponseDTO.userId,
      id = postResponseDTO.id,
      title = postResponseDTO.title,
      body = postResponseDTO.body
    )
  }
}
