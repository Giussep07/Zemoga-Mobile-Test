package com.giussepr.zemoga.data.mapper

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
}
