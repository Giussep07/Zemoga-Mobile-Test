package com.giussepr.zemoga.presentation.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.giussepr.zemoga.R
import com.giussepr.zemoga.databinding.FragmentPostDetailBinding
import com.giussepr.zemoga.presentation.base.BaseFragment
import com.giussepr.zemoga.presentation.postdetail.adapter.CommentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {

  private val args: PostDetailFragmentArgs by navArgs()

  private val viewModel: PostDetailViewModel by viewModels()

  private lateinit var adapter: CommentAdapter

  override fun bindView(
    inflater: LayoutInflater,
    container: ViewGroup?
  ): FragmentPostDetailBinding {
    return FragmentPostDetailBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.init(args.uiPost)
    viewModel.getAuthorInformation(args.uiPost.userId)

    setupAdapter()
    setupPostDetail()
    viewModel.getComments(args.uiPost.id)

    observeUserInfoUiState()
    observeCommentUiState()
    observePostSavedAsFavorite()

    binding.ibFavorite.setOnClickListener {
      viewModel.onFavoriteClicked(args.uiPost)
    }
  }

  private fun setupAdapter() {
    adapter = CommentAdapter().also {
      binding.rvComments.adapter = it
    }
  }

  private fun setupPostDetail() {
    binding.tvPostTitle.text = args.uiPost.title
    binding.tvPostBody.text = args.uiPost.body
  }

  private fun observeUserInfoUiState() {
    viewModel.userInformationUiState.collectWhileResumed {
      when (it) {
        is PostDetailViewModel.UserInformationUiState.Loading -> {
          binding.shimmerLayout.startShimmer()
        }
        is PostDetailViewModel.UserInformationUiState.Success -> {
          binding.shimmerLayout.hideShimmer()
          binding.tvAuthorName.text = it.user.name
          binding.tvAuthorUsername.text = it.user.username
          binding.tvAuthorEmail.text = it.user.email
          binding.tvAuthorWeb.text = it.user.website
        }
        is PostDetailViewModel.UserInformationUiState.Error -> {
          binding.shimmerLayout.stopShimmer()
        }
      }
    }
  }

  private fun observeCommentUiState() {
    viewModel.commentUiState.collectWhileResumed {
      when (it) {
        is PostDetailViewModel.CommentsUiState.Loading -> {
          binding.commentsProgressBar.isVisible = true
        }
        is PostDetailViewModel.CommentsUiState.Success -> {
          if (it.comments.isEmpty()) {
            binding.tvNoComments.isVisible = true
          } else {
            adapter.setData(it.comments)
          }
          binding.commentsProgressBar.isVisible = false
        }
        is PostDetailViewModel.CommentsUiState.Error -> {
          binding.commentsProgressBar.isVisible = false
        }
      }
    }
  }

  private fun observePostSavedAsFavorite() {
    viewModel.postIsFavorite.collectWhileResumed {
      if (it) {
        binding.ibFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_filled, requireContext().theme))
      } else {
        binding.ibFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border, requireContext().theme))
      }
    }
  }
}
