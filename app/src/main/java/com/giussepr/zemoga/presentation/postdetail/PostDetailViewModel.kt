package com.giussepr.zemoga.presentation.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giussepr.zemoga.domain.model.Comment
import com.giussepr.zemoga.domain.model.User
import com.giussepr.zemoga.domain.model.fold
import com.giussepr.zemoga.domain.usecase.GetAuthorInformationUseCase
import com.giussepr.zemoga.domain.usecase.GetCommentsByPostIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
  private val getAuthorInformationUseCase: GetAuthorInformationUseCase,
  private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase
) : ViewModel() {

  private val _userInformationUiState: MutableStateFlow<UserInformationUiState> =
    MutableStateFlow(UserInformationUiState.Loading)
  val userInformationUiState: StateFlow<UserInformationUiState>
    get() = _userInformationUiState

  private val _commentUiState: MutableStateFlow<CommentsUiState> =
    MutableStateFlow(CommentsUiState.Loading)
  val commentUiState: StateFlow<CommentsUiState>
    get() = _commentUiState

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
}
