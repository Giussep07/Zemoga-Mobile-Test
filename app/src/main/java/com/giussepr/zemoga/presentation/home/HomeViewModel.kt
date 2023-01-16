package com.giussepr.zemoga.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giussepr.zemoga.domain.model.fold
import com.giussepr.zemoga.domain.usecase.DeleteAllPostsUseCase
import com.giussepr.zemoga.domain.usecase.GetAllPostsUseCase
import com.giussepr.zemoga.presentation.model.UiPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getAllPostsUseCase: GetAllPostsUseCase,
  private val deleteAllPostsUseCase: DeleteAllPostsUseCase
) : ViewModel() {

  private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
  val uiState: StateFlow<HomeUiState>
    get() = _uiState

  private val _deleteAllPostsUiState: MutableStateFlow<DeleteAllPostsUiState> =
    MutableStateFlow(DeleteAllPostsUiState.Idle)
  val deleteAllPostsUiState: StateFlow<DeleteAllPostsUiState>
    get() = _deleteAllPostsUiState

  fun getAllPosts() {
    getAllPostsUseCase().map { result ->
      result.fold(
        onSuccess = { posts ->
          _uiState.value = HomeUiState.Success(posts)
        },
        onFailure = {
          _uiState.value = HomeUiState.Error(it.message)
        })
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  fun onDeleteAllPostsClicked() {
    deleteAllPostsUseCase().map { result ->
      result.fold(
        onSuccess = { getAllPosts() },
        onFailure = { _deleteAllPostsUiState.value = DeleteAllPostsUiState.Error(it.message) })
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val posts: List<UiPost>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
  }

  sealed class DeleteAllPostsUiState {
    object Idle : DeleteAllPostsUiState()
    data class Error(val message: String) : DeleteAllPostsUiState()
  }
}
