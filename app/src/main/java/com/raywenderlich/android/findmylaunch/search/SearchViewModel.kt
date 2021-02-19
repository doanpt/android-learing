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

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.lifecycle.*
import com.raywenderlich.android.findmylaunch.db.Launch
import com.raywenderlich.android.findmylaunch.db.LaunchDao
import com.raywenderlich.android.findmylaunch.db.calculateScore
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import javax.inject.Inject
import javax.inject.Provider


class SearchViewModel(
        private val launchDao: LaunchDao
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Launch>>()
    val searchResults: LiveData<List<Launch>>
        get() = _searchResults

    init {
        fetchAllLaunches()
    }

    fun search(query: Editable?) {
        viewModelScope.launch {
            if (query.isNullOrBlank()) {
                launchDao.all().let {
                    _searchResults.postValue(it)
                }
            } else {
                val sanitizedQuery = sanitizeSearchQuery(query)
                Log.d("doanpt", "Query is: $sanitizedQuery")
                launchDao.search(sanitizedQuery).let {
                    _searchResults.postValue(it)
                }
            }
        }
    }


    private fun sanitizeSearchQuery(query: Editable?): String {
        if (query == null) {
            return "";
        }
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "*\"$queryWithEscapedQuotes\"*"
    }

    private fun fetchAllLaunches() {
        viewModelScope.launch {
            launchDao.all().let { _searchResults.postValue(it) }
        }
    }

    // Don't forget to import the calculateScore method

    fun searchWithScore(query: Editable?) {
        // 1
        viewModelScope.launch {
            // 2
            if (query.isNullOrBlank()) {
                // 3
                launchDao.all().let { _searchResults.postValue(it) }
            } else {
                // 4
                val sanitizedQuery = sanitizeSearchQuery(query)
                launchDao.searchWithMatchInfo(sanitizedQuery).let { results ->
                    // 5
                    results.sortedByDescending { result -> calculateScore(result.matchInfo) }
                            // 6
                            .map { result -> result.launch }
                            // 7
                            .let { _searchResults.postValue(it) }
                }
            }
        }
    }

    class Factory @Inject constructor(
            private val launchDao: Provider<LaunchDao>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(launchDao = launchDao.get()) as T
        }
    }
}