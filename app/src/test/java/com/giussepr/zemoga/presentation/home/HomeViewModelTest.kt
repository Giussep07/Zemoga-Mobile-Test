package com.giussepr.zemoga.presentation.home

import com.giussepr.zemoga.domain.model.DomainException
import com.giussepr.zemoga.domain.usecase.DeleteAllPostsUseCase
import com.giussepr.zemoga.domain.usecase.GetAllPostsUseCase
import com.giussepr.zemoga.domain.usecase.GetLocalPostsUseCase
import com.giussepr.zemoga.presentation.model.UiPost
import com.giussepr.zemoga.presentation.util.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import com.giussepr.zemoga.domain.model.Result
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

  @get:Rule
  val mainCoroutineRule = MainCoroutineRule()

  private lateinit var homeViewModel: HomeViewModel

  private val getAllPostsUseCase: GetAllPostsUseCase = mockk()
  private val deleteAllPostsUseCase: DeleteAllPostsUseCase = mockk()
  private val getLocalPostsUseCase: GetLocalPostsUseCase = mockk()

  @Before
  fun setUp() {
    homeViewModel = HomeViewModel(
      getAllPostsUseCase,
      deleteAllPostsUseCase,
      getLocalPostsUseCase
    )
  }

  @After
  fun tearDown() {
  }

  @Test
  fun `test getAllPosts() success`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: List<UiPost> = buildList {
      add(UiPost(userId = 1, id = 1, title = "title", body = "body", isFavorite = true))
      add(UiPost(userId = 1, id = 2, title = "title", body = "body", isFavorite = false))
    }

    every { getAllPostsUseCase(any()) } returns flowOf(Result.Success(expected))

    // execute
    homeViewModel.getAllPosts()

    // verify
    verify(exactly = 1) { getAllPostsUseCase(any()) }

    assertEquals(homeViewModel.uiState.value, HomeViewModel.HomeUiState.Success(expected))
  }

  @Test
  fun `test getAllPosts() failure`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: String = "Something went wrong"

    every { getAllPostsUseCase(any()) } returns flowOf(Result.Error(DomainException(expected)))

    // execute
    homeViewModel.getAllPosts()

    // verify
    verify(exactly = 1) { getAllPostsUseCase(any()) }

    assertEquals(homeViewModel.uiState.value, HomeViewModel.HomeUiState.Error(expected))
  }

  @Test
  fun `test onDeleteAllPostsClicked() success`() = mainCoroutineRule.runBlockingTest {
    // prepare
    every { deleteAllPostsUseCase() } returns flowOf(Result.Success(true))
    every { getLocalPostsUseCase() } returns flowOf()

    // execute
    homeViewModel.onDeleteAllPostsClicked()

    // verify
    verify(exactly = 1) {
      deleteAllPostsUseCase()
      getLocalPostsUseCase()
    }
  }

  @Test
  fun `test onDeleteAllPostsClicked() failure`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: String = "Something went wrong"
    every { deleteAllPostsUseCase() } returns flowOf(Result.Error(DomainException(expected)))
    every { getLocalPostsUseCase() } returns flowOf()

    // execute
    homeViewModel.onDeleteAllPostsClicked()

    // verify
    verify(exactly = 1) { deleteAllPostsUseCase() }

    assertEquals(homeViewModel.deleteAllPostsUiState.value, HomeViewModel.DeleteAllPostsUiState.Error(expected))
  }

  @Test
  fun `test getAllLocalPosts success`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: List<UiPost> = buildList {
      add(UiPost(userId = 1, id = 1, title = "title", body = "body", isFavorite = true))
      add(UiPost(userId = 1, id = 2, title = "title", body = "body", isFavorite = false))
    }

    every { getLocalPostsUseCase() } returns flowOf(Result.Success(expected))

    // execute
    homeViewModel.getAllLocalPosts()

    // verify
    verify(exactly = 1) { getLocalPostsUseCase() }

    assertEquals(homeViewModel.uiState.value, HomeViewModel.HomeUiState.Success(expected))
  }

  @Test
  fun `test getAllLocalPosts failure`() = mainCoroutineRule.runBlockingTest {
    // prepare
    val expected: String = "Something went wrong"

    every { getLocalPostsUseCase() } returns flowOf(Result.Error(DomainException(expected)))

    // execute
    homeViewModel.getAllLocalPosts()

    // verify
    verify(exactly = 1) { getLocalPostsUseCase() }

    assertEquals(homeViewModel.uiState.value, HomeViewModel.HomeUiState.Error(expected))
  }
}
