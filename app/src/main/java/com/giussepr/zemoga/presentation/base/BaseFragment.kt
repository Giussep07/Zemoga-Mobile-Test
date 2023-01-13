package com.giussepr.zemoga.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewBinding> : Fragment() {

  private var _binding: B? = null
  protected val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = bindView(inflater, container)
    return binding.root
  }

  abstract fun bindView(inflater: LayoutInflater, container: ViewGroup?): B

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  protected fun <T> Flow<T>.collectWhileResumed(collector: FlowCollector<T>): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
      flowWithLifecycle(
        viewLifecycleOwner.lifecycle,
        minActiveState = Lifecycle.State.RESUMED,
      ).collect(
        collector
      )
    }
  }
}
