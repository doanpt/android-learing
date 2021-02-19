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

package com.raywenderlich.android.findmylaunch.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.findmylaunch.R
import com.raywenderlich.android.findmylaunch.appComponent
import com.raywenderlich.android.findmylaunch.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

  private var _binding: FragmentDetailsBinding? = null
  private val binding: FragmentDetailsBinding
    get() = _binding!!

  private val viewModel: DetailsViewModel by viewModels {
    appComponent(requireContext()).detailsViewModelFactory()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    _binding = FragmentDetailsBinding.inflate(inflater, container, false)

    binding.toolbar.setNavigationOnClickListener {
      parentFragmentManager.popBackStack()
    }

    viewModel.fetchLaunchDetails(requireArguments().getString(keyLaunchName)!!)

    viewModel.launch.observe(viewLifecycleOwner) { launch ->
      binding.name.text = launch.name
      binding.details.text = launch.details.ifBlank { getString(R.string.no_details_text) }
      binding.toolbar.title = launch.name
    }

    return binding.root
  }

  companion object {

    const val keyLaunchName: String = "launchName"

    fun newInstance(launchName: String): DetailsFragment {
      return DetailsFragment().apply {
        arguments = bundleOf(keyLaunchName to launchName)
      }
    }
  }
}