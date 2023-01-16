package com.giussepr.zemoga.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

  private fun setAdapter() {
    adapter = PostAdapter(context = requireContext(), onPostClickListener = this::onPostClicked).also {
      binding.rvPosts.adapter = it
    }
  }

  private fun onPostClicked(uiPost: UiPost) {
    val action = HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(uiPost)
    this.findNavController().navigate(action)
  }

}
