package com.giussepr.zemoga.presentation.home

import android.os.Bundle
import android.view.*
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
import com.giussepr.zemoga.presentation.home.adapter.PostAdapter
import com.giussepr.zemoga.presentation.model.UiPost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private val viewModel: HomeViewModel by viewModels()

  private lateinit var adapter: PostAdapter

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    setHasOptionsMenu(true)
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getAllPosts()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.flowWithLifecycle(
        viewLifecycleOwner.lifecycle,
        minActiveState = Lifecycle.State.RESUMED
      ).collect {
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

    setAdapter()
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.home_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_delete_post -> {
        showDeleteAllPostsDialog()
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

}
