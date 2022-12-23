package com.jk.thymecard.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jk.thymecard.Daily
import com.jk.thymecard.DailyRepository
import java.time.Instant

class InputViewModel(private val repository: DailyRepository) : ViewModel() {
    var complete: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var punchInSet: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var punchOutSet: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private var timeInH: Int = 0
    private var timeInM: Int = 0
    private var timeOutH: Int = 0
    private var timeOutM: Int = 0
    private var milesIn: Int = 0
    private var milesOut: Int = 0
    private var initialStops = 0
    private var actualStops = 0
    private var initialPkgs = 0
    private var actualPkgs = 0

    fun setPunchIn(hour: Int, minute: Int, miles: Int, stops: Int, pkgs: Int) {
        timeInH = hour
        timeInM = minute
        milesIn = miles
        initialStops = stops
        initialPkgs = pkgs
        setSection("in")
    }

    fun setPunchOut(hour: Int, minute: Int, miles: Int, stops: Int, pkgs: Int) {
        timeOutH = hour
        timeOutM = minute
        milesOut = miles
        actualStops = stops
        actualPkgs = pkgs
        setSection("out")
    }

    private fun setSection(which: String) {
        when (which) {
            "in" -> {
                punchInSet.postValue(true)
                if (punchOutSet.value == true) complete.postValue(true)
            }
            "out" -> {
                punchOutSet.postValue(true)
                if (punchInSet.value == true) complete.postValue(true)
            }
        }
    }

    fun unSetSection(which: String) {
        when (which) {
            "in" -> {
                punchInSet.postValue(false)
            }
            "out" -> {
                punchOutSet.postValue(false)
            }
        }
        complete.postValue(false)
    }

    suspend fun submitDaily() {
        repository.insert(Daily(
            System.currentTimeMillis(),
            26, 12, 1970,
            timeInH, timeInM, timeOutH, timeOutM, 0.0, 0.0,
            milesIn, milesOut, 0.0,
            initialStops, actualStops, initialPkgs, actualPkgs,
            Instant.now(),
            Instant.now()
        ))
    }

    fun deleteDaily(daily: Daily) {
        repository.delete(daily)
    }

    fun allDailies(): LiveData<List<Daily>> {
        return repository.allWords
    }
}