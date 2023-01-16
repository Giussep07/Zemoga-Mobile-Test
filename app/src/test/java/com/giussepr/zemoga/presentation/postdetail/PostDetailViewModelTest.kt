package com.giussepr.zemoga.presentation.postdetail

import com.giussepr.zemoga.domain.model.Comment
import com.giussepr.zemoga.domain.model.DomainException
import com.giussepr.zemoga.domain.model.User
import com.giussepr.zemoga.domain.usecase.*
import com.giussepr.zemoga.presentation.model.UiPost
import com.giussepr.zemoga.presentation.util.MainCoroutineRule
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.giussepr.zemoga.domain.model.Result
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals

class PostDetailViewModelTest {

  @get:Rule
  val mainCoroutineRule = MainCoroutineRule()

  private lateinit var postDetailViewModel: PostDetailViewModel

  private val getAuthorInformationUseCase: GetAuthorInformationUseCase = mockk()
  private val getCommentsByPostIdUseCase: GetCommentsByPostIdUseCase = mockk()
  private val checkIfPostIsFavoriteUseCase: CheckIfPostIsFavoriteUseCase = mockk()
  private val setPostAsFavoriteUseCase: SetPostAsFavoriteUseCase = mockk()
  private val deletePostUseCase: DeletePostUseCase = mockk()
  private val _postIsFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)

  @Before
  fun setUp() {
    postDetailViewModel = PostDetailViewModel(
      getAuthorInformationUseCase,
      getCommentsByPostIdUseCase,
      checkIfPostIsFavoriteUseCase,
      setPostAsFavoriteUseCase,
      deletePostUseCase,
      _postIsFavorite
    )
  }

  @After
  fun tearDown() {
  }

  @Test
  fun `test init()`() = mainCoroutineRule.runBlockingTest {
    // prepare
    every { checkIfPostIsFavoriteUseCase(any()) } returns flowOf()

    // execute
    postDetailViewModel.init(
      post = UiPost(
        userId = 1,
        id = 1,
        title = "title",
        body = "body",
        isFavorite = true
      )
    )

    // verify
    verify(exactly = 1) { checkIfPostIsFavoriteUseCase(any()) }
  }

  @Test
  fun `test getAuthorInformation() success`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: User = User(
      id = 1,
      name = "name",
      username = "username",
      email = "email",
      phone = "phone",
      website = "website",
    )

    every { getAuthorInformationUseCase(any()) } returns flowOf(Result.Success(expected))

    // execute
    postDetailViewModel.getAuthorInformation(userId = 1)

    // verify
    verify(exactly = 1) { getAuthorInformationUseCase(any()) }

    assertEquals(PostDetailViewModel.UserInformationUiState.Success(expected), postDetailViewModel.userInformationUiState.value)
  }

  @Test
  fun `test getAuthorInformation() error`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: String = "Something went wrong"

    every { getAuthorInformationUseCase(any()) } returns flowOf(Result.Error(DomainException(expected)))

    // execute
    postDetailViewModel.getAuthorInformation(userId = 1)

    // verify
    verify(exactly = 1) { getAuthorInformationUseCase(any()) }

    assertEquals(PostDetailViewModel.UserInformationUiState.Error(expected), postDetailViewModel.userInformationUiState.value)
  }

  @Test
  fun `test getComments() success`() {
    // prepare
    val expected: List<Comment> = buildList {
      add(Comment(
        postId = 1,
        id = 1,
        name = "name",
        email = "email",
        body = "body"
      ))
    }

    every { getCommentsByPostIdUseCase(any()) } returns flowOf(Result.Success(expected))

    // execute
    postDetailViewModel.getComments(postId = 1)

    // verify
    verify(exactly = 1) { getCommentsByPostIdUseCase(any()) }

    assertEquals(PostDetailViewModel.CommentsUiState.Success(expected), postDetailViewModel.commentUiState.value)
  }

  @Test
  fun `test getComments() error`() {
    // prepare
    val expected: String = "Something went wrong"

    every { getCommentsByPostIdUseCase(any()) } returns flowOf(Result.Error(DomainException(expected)))

    // execute
    postDetailViewModel.getComments(postId = 1)

    // verify
    verify(exactly = 1) { getCommentsByPostIdUseCase(any()) }

    assertEquals(PostDetailViewModel.CommentsUiState.Error(expected), postDetailViewModel.commentUiState.value)
  }

  @Test
  fun `test setPostAsFavorite() mark post as favorite`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: Boolean = true

    every { setPostAsFavoriteUseCase(any(), any()) } returns flowOf(Result.Success(true))
    _postIsFavorite.value = false

    // execute
    postDetailViewModel.onFavoriteClicked(uiPost = UiPost(
      userId = 1,
      id = 1,
      title = "title",
      body = "body",
      isFavorite = false
    ))

    // verify
    verify(exactly = 1) { setPostAsFavoriteUseCase(any(), any()) }

    assertEquals(expected, _postIsFavorite.value)
  }

  @Test
  fun `test setPostAsFavorite() mark post no favorite `() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: Boolean = false

    every { setPostAsFavoriteUseCase(any(), any()) } returns flowOf(Result.Success(true))
    _postIsFavorite.value = true

    // execute
    postDetailViewModel.onFavoriteClicked(uiPost = UiPost(
      userId = 1,
      id = 1,
      title = "title",
      body = "body",
      isFavorite = true
    ))

    // verify
    verify(exactly = 1) { setPostAsFavoriteUseCase(any(), any()) }

    assertEquals(expected, postDetailViewModel.postIsFavorite.value)
  }

  @Test
  fun `test checkIfPostIsFavorite() post is favorite`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: Boolean = true

    every { checkIfPostIsFavoriteUseCase(any()) } returns flowOf(Result.Success(true))

    // execute
    postDetailViewModel.checkIfPostIsFavorite(postId = 1)

    // verify
    verify(exactly = 1) { checkIfPostIsFavoriteUseCase(any()) }

    assertEquals(expected, _postIsFavorite.value)
  }

  @Test
  fun `test checkIfPostIsFavorite() post is not favorite`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: Boolean = false

    every { checkIfPostIsFavoriteUseCase(any()) } returns flowOf(Result.Success(false))

    // execute
    postDetailViewModel.checkIfPostIsFavorite(postId = 1)

    // verify
    verify(exactly = 1) { checkIfPostIsFavoriteUseCase(any()) }

    assertEquals(expected, _postIsFavorite.value)
  }

  @Test
  fun `test onDeletePostClicked() success delete`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected = PostDetailViewModel.DeletePostUiState.Success

    every { deletePostUseCase(any()) } returns flowOf(Result.Success(true))

    // execute
    postDetailViewModel.onDeletePostClicked(
      uiPost = UiPost(
        userId = 1,
        id = 1,
        title = "title",
        body = "body",
        isFavorite = true
      )
    )

    // verify
    verify(exactly = 1) { deletePostUseCase(any()) }

    assertEquals(expected, postDetailViewModel.deletePostUiState.value)
  }

  @Test
  fun `test onDeletePostClicked() error`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected = "Something went wrong"

    every { deletePostUseCase(any()) } returns flowOf(Result.Error(DomainException(expected)))

    // execute
    postDetailViewModel.onDeletePostClicked(
      uiPost = UiPost(
        userId = 1,
        id = 1,
        title = "title",
        body = "body",
        isFavorite = true
      )
    )

    // verify
    verify(exactly = 1) { deletePostUseCase(any()) }

    assertEquals(PostDetailViewModel.DeletePostUiState.Error(expected), postDetailViewModel.deletePostUiState.value)
  }
}
