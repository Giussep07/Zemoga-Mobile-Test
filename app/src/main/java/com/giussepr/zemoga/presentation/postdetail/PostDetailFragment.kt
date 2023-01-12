package com.giussepr.zemoga.presentation.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.giussepr.zemoga.R

class PostDetailFragment : Fragment() {

  private val args: PostDetailFragmentArgs by navArgs()

  private lateinit var viewModel: PostDetailViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_post_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    println("Post detail: ${args.uiPost}")
  }

}
