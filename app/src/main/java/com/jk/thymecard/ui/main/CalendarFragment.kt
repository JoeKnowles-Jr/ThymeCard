package com.jk.thymecard.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jk.thymecard.Constants
import com.jk.thymecard.Daily
import com.jk.thymecard.DailyDatabase
import com.jk.thymecard.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    private lateinit var viewModel: InputViewModel
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var mDailies: List<Daily>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao by lazy { DailyDatabase.getDatabase(requireContext()).dailyDao() }
        viewModel = ViewModelProvider(this, InputViewModelFactory(dao))[InputViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allDailies().observe(viewLifecycleOwner) {
            mDailies = it
        }

        binding.cvCal.setOnDateChangeListener { calendarView, year, monthOfYear, dayOfMonth ->
            val day = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val month = Constants.MONTHS[monthOfYear]
            val text = "$day-$month-$year"
            binding.tvCal.text = text
        }
    }
}