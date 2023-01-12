package com.giussepr.zemoga.domain.mapper

import com.giussepr.zemoga.domain.model.Post
import com.giussepr.zemoga.presentation.model.UiPost

class PostMapper {

  fun mapToUiPost(post: Post) = UiPost(
    userId = post.userId,
    id = post.id,
    title = post.title,
    body = post.body
  )
}
