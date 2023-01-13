package com.giussepr.zemoga.presentation.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.giussepr.zemoga.databinding.FragmentPostDetailBinding
import com.giussepr.zemoga.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {

  private val args: PostDetailFragmentArgs by navArgs()

  private val viewModel: PostDetailViewModel by viewModels()

  override fun bindView(
    inflater: LayoutInflater,
    container: ViewGroup?
  ): FragmentPostDetailBinding {
    return FragmentPostDetailBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.getAuthorInformation(args.uiPost.userId)

    setupPostDetail()

    observeUserInfoUiState()
  }

  private fun setupPostDetail() {
    binding.tvPostTitle.text = args.uiPost.title
    binding.tvPostBody.text = args.uiPost.body
  }

  private fun observeUserInfoUiState() {
    viewModel.userInformationUiState.collectWhileResumed {
      when (it) {
        is PostDetailViewModel.UserInformationUiState.Loading -> {
          //binding.progressBar.isVisible = true
        }
        is PostDetailViewModel.UserInformationUiState.Success -> {
          //binding.progressBar.isVisible = false
          binding.tvAuthorName.text = it.user.name
          binding.tvAuthorUsername.text = it.user.username
          binding.tvAuthorEmail.text = it.user.email
          binding.tvAuthorWeb.text = it.user.website
        }
        is PostDetailViewModel.UserInformationUiState.Error -> {
          //binding.progressBar.isVisible = false
        }
      }
    }
  }
}
