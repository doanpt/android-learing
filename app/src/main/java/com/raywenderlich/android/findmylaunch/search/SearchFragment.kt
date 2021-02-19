/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.findmylaunch.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.findmylaunch.R
import com.raywenderlich.android.findmylaunch.appComponent
import com.raywenderlich.android.findmylaunch.databinding.FragmentSearchBinding
import com.raywenderlich.android.findmylaunch.details.DetailsFragment

class SearchFragment : Fragment() {

  private var _binding: FragmentSearchBinding? = null
  private val binding: FragmentSearchBinding
    get() = _binding!!

  private val viewModel: SearchViewModel by viewModels {
    appComponent(requireContext()).searchViewModelFactory()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    _binding = FragmentSearchBinding.inflate(inflater, container, false)

    setupSearchResults()
    setupSearchField()
    setupBackNavigation()

    return binding.root
  }

  private fun setupSearchField() {
    binding.searchBox.addTextChangedListener { query ->
      viewModel.search(query)
    }
  }

  private fun setupBackNavigation() {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
      if (!binding.searchBox.text.isNullOrBlank()) {
        binding.searchBox.text = null
      } else {
        isEnabled = false
        requireActivity().onBackPressed()
      }
    }
  }

  private fun setupSearchResults() {
    val searchResultsAdapter = SearchResultsAdapter { launch ->
      parentFragmentManager.commit {
        setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        replace(R.id.fragmentContainer, DetailsFragment.newInstance(launch.name))
        addToBackStack(launch.name)
      }
    }

    binding.searchResults.apply {
      layoutManager = LinearLayoutManager(this.context)
      adapter = searchResultsAdapter
    }

    viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
      searchResultsAdapter.submitList(searchResults)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}