package com.giussepr.zemoga.data.mapper

import com.giussepr.zemoga.data.database.entity.PostEntity
import com.giussepr.zemoga.data.model.PostResponseDTO
import com.giussepr.zemoga.domain.model.Post

class PostDataMapper {

  fun mapResponseToPostDomain(postResponseDTO: PostResponseDTO): Post {
    return Post(
      userId = postResponseDTO.userId,
      id = postResponseDTO.id,
      title = postResponseDTO.title,
      body = postResponseDTO.body,
      isFavorite = false
    )
  }

  fun mapEntityToPostDomain(postEntity: PostEntity): Post {
    return Post(
      userId = postEntity.userId,
      id = postEntity.postId,
      title = postEntity.title,
      body = postEntity.body,
      isFavorite = postEntity.isFavorite
    )
  }

  fun mapResponseToPostEntity(postResponseDTO: PostResponseDTO): PostEntity {
    return PostEntity(
      postId = postResponseDTO.id,
      userId = postResponseDTO.userId,
      title = postResponseDTO.title,
      body = postResponseDTO.body,
      isFavorite = false
    )
  }
}
