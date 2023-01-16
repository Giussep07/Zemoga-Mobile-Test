package com.giussepr.zemoga.presentation.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giussepr.zemoga.domain.model.Comment
import com.giussepr.zemoga.domain.model.User
import com.giussepr.zemoga.domain.model.fold
import com.giussepr.zemoga.domain.usecase.*
import com.giussepr.zemoga.presentation.model.UiPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
  private val getAuthorInformationUseCase: GetAuthorInformationUseCase,
  private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase,
  private val checkIfPostIsFavoriteUseCase: CheckIfPostIsFavoriteUseCase,
  private val setPostAsFavoriteUseCase: SetPostAsFavoriteUseCase,
  private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

  private val _userInformationUiState: MutableStateFlow<UserInformationUiState> =
    MutableStateFlow(UserInformationUiState.Loading)
  val userInformationUiState: StateFlow<UserInformationUiState>
    get() = _userInformationUiState

  private val _commentUiState: MutableStateFlow<CommentsUiState> =
    MutableStateFlow(CommentsUiState.Loading)
  val commentUiState: StateFlow<CommentsUiState>
    get() = _commentUiState

  private val _postIsFavorite: MutableStateFlow<Boolean> =
    MutableStateFlow(false)
  val postIsFavorite: StateFlow<Boolean>
    get() = _postIsFavorite

  private val _deletePostUiState: MutableStateFlow<DeletePostUiState> =
    MutableStateFlow(DeletePostUiState.Idle)
  val deletePostUiState: StateFlow<DeletePostUiState>
    get() = _deletePostUiState

  fun init(post: UiPost) {
    checkIfPostIsFavorite(post.id)
  }

  fun getAuthorInformation(userId: Int) {
    getAuthorInformationUseCase(userId).map { result ->
      result.fold(
        onSuccess = { _userInformationUiState.value = UserInformationUiState.Success(it) },
        onFailure = { _userInformationUiState.value = UserInformationUiState.Error(it.message) }
      )
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  fun getComments(postId: Int) {
    getCommentsByPostIdUseCase(postId).map { result ->
      result.fold(
        onSuccess = { _commentUiState.value = CommentsUiState.Success(it) },
        onFailure = { _commentUiState.value = CommentsUiState.Error(it.message) }
      )
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  fun onFavoriteClicked(uiPost: UiPost) {
    val isFavorite = !_postIsFavorite.value
    setPostAsFavoriteUseCase(uiPost.id, isFavorite).map { result ->
      result.fold(
        onSuccess = { _postIsFavorite.value = isFavorite },
        onFailure = { /* Should not happen */ }
      )
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  private fun checkIfPostIsFavorite(postId: Int) {
    checkIfPostIsFavoriteUseCase(postId).map { result ->
      result.fold(
        onSuccess = { _postIsFavorite.value = it },
        onFailure = { /* Should not happen */ }
      )
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  fun onDeletePostClicked(uiPost: UiPost) {
    deletePostUseCase(uiPost.id).map { result ->
      result.fold(
        onSuccess = { _deletePostUiState.value = DeletePostUiState.Success },
        onFailure = { _deletePostUiState.value = DeletePostUiState.Error(it.message) }
      )
    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
  }

  sealed class UserInformationUiState {
    object Loading : UserInformationUiState()
    data class Success(val user: User) : UserInformationUiState()
    data class Error(val message: String) : UserInformationUiState()
  }

  sealed class CommentsUiState {
    object Loading : CommentsUiState()
    data class Success(val comments: List<Comment>) : CommentsUiState()
    data class Error(val message: String) : CommentsUiState()
  }

  sealed class DeletePostUiState {
    object Idle : DeletePostUiState()
    object Success : DeletePostUiState()
    data class Error(val message: String) : DeletePostUiState()
  }
}
