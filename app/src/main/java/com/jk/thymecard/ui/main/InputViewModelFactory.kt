package com.jk.thymecard.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jk.thymecard.DailyDao
import com.jk.thymecard.DailyDatabase
import com.jk.thymecard.DailyRepository

class InputViewModelFactory(private val dao: DailyDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
            val repository by lazy { DailyRepository(dao) }
            @Suppress("UNCHECKED_CAST")
            return InputViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}