package com.giussepr.zemoga.data.mapper

import com.giussepr.zemoga.data.database.entity.CommentEntity
import com.giussepr.zemoga.data.model.CommentResponseDTO
import com.giussepr.zemoga.domain.model.Comment

class CommentResponseMapper {

  fun mapToDomainComment(commentResponse: CommentResponseDTO) = Comment(
    postId = commentResponse.postId,
    id = commentResponse.id,
    name = commentResponse.name,
    email = commentResponse.email,
    body = commentResponse.body
  )

  fun mapResponseToEntity(responseDTO: CommentResponseDTO): CommentEntity {
    return CommentEntity(
      id = responseDTO.id,
      postId = responseDTO.postId,
      name = responseDTO.name,
      email = responseDTO.email,
      body = responseDTO.body
    )
  }

  fun mapEntityToDomainComment(entity: CommentEntity): Comment {
    return Comment(
      postId = entity.postId,
      id = entity.id,
      name = entity.name,
      email = entity.email,
      body = entity.body
    )
  }
}
