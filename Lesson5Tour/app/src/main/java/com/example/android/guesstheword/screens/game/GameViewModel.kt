package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.event.Event

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }


    private val timer: CountDownTimer

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word
    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private var _time = MutableLiveData<Long>()
    val time: LiveData<Long>
        get() = _time
    //Transformations is class that transfer a live data to another live data
    val currentTimeString = Transformations.map(time) { time ->
        DateUtils.formatElapsedTime(time)
    }
    //https://proandroiddev.com/livedata-with-single-events-2395dea972a8
    //https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
    //https://code.luasoftware.com/tutorials/android/android-singleliveevent-for-ui-event/
    //https://viblo.asia/p/livedata-voi-singleliveevent-va-event-wrapper-jvElaG34Kkw
    private val _eventGameFinish = MutableLiveData<Event<Boolean>>()
    val eventGameFinish: LiveData<Event<Boolean>>
        get() = _eventGameFinish

    init {
        Log.i("GameViewModel", "Game view model created")
        resetList()
        nextWord()
        _score.value = 0
        _eventGameFinish.value = Event(false)
        //object is class(Anonymous class)
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _time.value = DONE
                _eventBuzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = Event(true)
            }

            override fun onTick(millisUntilFinished: Long) {
                _time.value = millisUntilFinished / ONE_SECOND
                if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }
        }
        timer.start()
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "Game view model cleared")
        timer.cancel()
    }

    //FIXME use Event same with _eventGameFinish
    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}