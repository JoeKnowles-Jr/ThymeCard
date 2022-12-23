package com.jk.thymecard.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.jk.thymecard.*
import com.jk.thymecard.databinding.FragmentMainBinding
import java.text.DecimalFormat

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: InputViewModel
    private var _binding: FragmentMainBinding? = null
    private val mAdapter: DailyListAdapter = DailyListAdapter()

    private val binding get() = _binding!!

    private val df = DecimalFormat("#,##0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao by lazy { DailyDatabase.getDatabase(requireContext()).dailyDao() }
        viewModel = ViewModelProvider(this, InputViewModelFactory(dao))[InputViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.listenWith(object: DailyListAdapter.DailyClickListener {
            override fun onDailyClick(daily: Daily) {
                println("Clicked")
            }

            override fun onDailyLongClick(daily: Daily) {
                viewModel.deleteDaily(daily)
            }
        })
        binding.rvMain.adapter = mAdapter
        viewModel.allDailies().observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
            var total = 0.0
            it.forEach { daily ->
                total += daily.getTotalPay()
            }
            val tp = df.format(total)
            val tpf = "$$tp"
            binding.tvAccumulatedPay.text = tpf
        }
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        binding.btnWeek.setOnClickListener {
            binding.cvMain.visibility = View.VISIBLE
        }

        binding.btnCal.setOnClickListener {
            (requireActivity() as MainActivity).showCalendar()
        }
    }
}