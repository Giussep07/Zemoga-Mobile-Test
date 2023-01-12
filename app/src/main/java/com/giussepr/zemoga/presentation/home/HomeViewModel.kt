package com.giussepr.zemoga.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giussepr.zemoga.domain.model.fold
import com.giussepr.zemoga.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllPostsUseCase: GetAllPostsUseCase) :
  ViewModel() {

  fun getAllPosts() {
    getAllPostsUseCase().map { result ->
      result.fold(
        onSuccess = { posts -> println("All posts: $posts") },
        onFailure = {
          println("Error: ${it.message}")
        })
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }
}
