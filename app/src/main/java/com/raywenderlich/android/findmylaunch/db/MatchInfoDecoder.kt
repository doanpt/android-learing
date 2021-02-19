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

package com.raywenderlich.android.findmylaunch.db

/**
 * Calculates the relevance score of a search result from its `matchinfo` blob
 * value returned by SQLite.
 *
 * The `matchinfo` function in SQLite returns a sequence of bytes whose size depends
 * on the configuration string supplied to it. The default configuration string is "pcx".
 *
 * - 'p' represents one integer value corresponding to the number of matchable phrases
 * in the query
 * - 'c' represents one integer value corresponding to the number of matchable columns
 * in the FTS table
 * - 'x' represents a series of bytes which contain various parameters describing the
 * match.
 *
 * The series of integers returned by 'x' can be used to calculate the relevance score
 * of the match. The series can be interpreted as chunks of three integers:
 * - The first int represents the number of matches of the phrase in the current column
 * for this row
 * - The second int represents the total number of matches of the phrase in the current
 * column for all rows
 *
 * These two values can be used to calculate a score for each column in each row using the
 * following equation:
 *  score = (hits in this row) / (hits in all rows)
 *
 * Adding this score for each column in a row gives the score for the entire row.
 *
 * For more information on the implementation, see https://www.sqlite.org/fts3.html#matchinfo
 */
fun calculateScore(matchInfo: ByteArray): Double {
  val info = matchInfo.toIntArray()

  val numPhrases = info[0]
  val numColumns = info[1]

  var score = 0.0
  for (phrase in 0 until numPhrases) {
    val offset = 2 + phrase * numColumns * 3
    for (column in 0 until numColumns) {
      val numHitsInRow = info[offset + 3 * column]
      val numHitsInAllRows = info[offset + 3 * column + 1]
      if (numHitsInAllRows > 0) {
        score += numHitsInRow.toDouble() / numHitsInAllRows.toDouble()
      }
    }
  }

  return score
}

/**
 * Converts the array of bytes representing the `matchinfo` for a search result
 * into an array of integers for easy consumption.
 *
 * This sequence returned by SQLite represents 32 bit integers using 4 bytes,
 * which can be converted to an [Int] value using a [java.nio.ByteBuffer].
 *
 * However, Room already does the parsing for us and gives us an [ByteArray]
 * in which the first byte represents the int value of the 32-bit integer,
 * while the remaining three bytes are 0.
 *
 * Therefore to convert this [ByteArray] to an [IntArray], we only
 * need to skip the three zeroes after each byte and return the result.
 */
fun ByteArray.toIntArray(skipSize: Int = 4): IntArray {
  val cleanedArr = IntArray(this.size / skipSize)
  for ((pointer, i) in (this.indices step skipSize).withIndex()) {
    cleanedArr[pointer] = this[i].toInt()
  }

  return cleanedArr
}