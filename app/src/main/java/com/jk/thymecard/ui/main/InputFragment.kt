package com.jk.thymecard.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jk.thymecard.Daily
import com.jk.thymecard.DailyDatabase
import com.jk.thymecard.MainActivity
import com.jk.thymecard.databinding.FragmentInputBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class InputFragment : Fragment() {

    companion object {
        fun newInstance() = InputFragment()
    }

    private lateinit var viewModel: InputViewModel
    private var _binding: FragmentInputBinding? = null
    private val mDaily: Daily = Daily(
        System.currentTimeMillis(),
        26, 11, 1970,
        9, 15,
        5, 30,
        0.0, 0.0,
        2222, 2333,
        0.0,
        130, 200,
        120, 180,
        Instant.now(), Instant.now()
    )

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao by lazy { DailyDatabase.getDatabase(requireContext()).dailyDao() }
        viewModel = ViewModelProvider(this, InputViewModelFactory(dao))[InputViewModel::class.java]
        binding.btnPunchIn.setOnClickListener {
            val hour = binding.timeInPicker.hour
            val minute = binding.timeInPicker.minute
            val m = binding.etMilesIn.text.toString()
            val miles = if (m == "") 0 else m.toInt()
            val s = binding.etInitialStops.text.toString()
            val stops = if (s == "") 0 else s.toInt()
            val p = binding.etInitialPkgs.text.toString()
            val pkgs = if (p == "") 0 else p.toInt()
            viewModel.setPunchIn(
                hour,
                minute,
                miles,
                stops,
                pkgs
            )
            mDaily.timeInH = hour
            mDaily.timeInM = minute
            binding.tvTimeIn.text = mDaily.getHoursWorked()
        }

        binding.btnPunchOut.setOnClickListener {
            val hour = binding.timeOutPicker.hour
            val minute = binding.timeOutPicker.minute
            val m = binding.etMilesOut.text.toString()
            val miles = if (m == "") 0 else m.toInt()
            val s = binding.etActualStops.text.toString()
            val stops = if (s == "") 0 else s.toInt()
            val p = binding.etActualPkgs.text.toString()
            val pkgs = if (p == "") 0 else p.toInt()
            viewModel.setPunchOut(
                hour,
                minute,
                miles,
                stops,
                pkgs
            )
            mDaily.timeOutH = hour
            mDaily.timeOutM = minute
            binding.tvTimeIn.text = mDaily.getHoursWorked()
        }

        binding.buttonSubmit.setOnClickListener {
            runBlocking {
                launch { viewModel.submitDaily() }
            }
            (requireActivity() as MainActivity).showList()
        }

        viewModel.complete.observe(viewLifecycleOwner) {
            binding.buttonSubmit.isEnabled = it
        }
    }



}