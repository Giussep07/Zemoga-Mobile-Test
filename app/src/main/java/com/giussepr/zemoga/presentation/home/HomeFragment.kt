package com.giussepr.zemoga.presentation.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giussepr.zemoga.R
import com.giussepr.zemoga.databinding.FragmentHomeBinding
import com.giussepr.zemoga.presentation.base.BaseFragment
import com.giussepr.zemoga.presentation.home.adapter.PostAdapter
import com.giussepr.zemoga.presentation.model.UiPost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

  private val viewModel: HomeViewModel by viewModels()

  private lateinit var adapter: PostAdapter

  override fun bindView(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
    setHasOptionsMenu(true)
    return FragmentHomeBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getAllPosts()

    observeHomeUiState()
    observeDeleteAllPostsUiState()

    setAdapter()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.home_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_delete_all -> {
        showDeleteAllPostsDialog()
        true
      }
      R.id.action_load_all_posts -> {
        viewModel.getAllPosts(forceRefresh = true)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun setAdapter() {
    adapter = PostAdapter(context = requireContext(), onPostClickListener = this::onPostClicked).also {
      binding.rvPosts.adapter = it
    }
  }

  private fun onPostClicked(uiPost: UiPost) {
    val action = HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(uiPost)
    this.findNavController().navigate(action)
  }

  private fun showDeleteAllPostsDialog() {
    AlertDialog.Builder(requireContext())
      .setMessage(getString(R.string.delete_all_posts_confirmation))
      .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.onDeleteAllPostsClicked() }
      .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
      .show()
  }

  private fun observeHomeUiState() {
    viewModel.uiState.collectWhileResumed {
      when (it) {
        is HomeViewModel.HomeUiState.Loading -> {
          binding.progressBar.isVisible = true
        }
        is HomeViewModel.HomeUiState.Success -> {
          binding.progressBar.isVisible = false
          adapter.setData(it.posts)
        }
        is HomeViewModel.HomeUiState.Error -> {
          binding.progressBar.visibility = View.GONE
        }
      }
    }
  }

  private fun observeDeleteAllPostsUiState() {
    viewModel.deleteAllPostsUiState.collectWhileResumed {
      when (it) {
        is HomeViewModel.DeleteAllPostsUiState.Error -> {
          Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
      }
    }
  }

}
