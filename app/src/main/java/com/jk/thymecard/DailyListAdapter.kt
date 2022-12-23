package com.jk.thymecard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DailyListAdapter : ListAdapter<Daily, DailyListAdapter.DailyViewHolder>(DailyViewHolder.DailyComparator()) {
    private lateinit var mListener: DailyClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder.create(parent, mListener)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class DailyViewHolder(itemView: View, val listener: DailyClickListener) : RecyclerView.ViewHolder(itemView) {
        private val dailyDate: TextView = itemView.findViewById(R.id.tv_date)
        private val dailyPay: TextView = itemView.findViewById(R.id.tv_total_pay)
        private val dailyTimeIn: TextView = itemView.findViewById(R.id.tv_time_in)
        private val dailyTimeOut: TextView = itemView.findViewById(R.id.tv_time_out)
        private val dailyMilesIn: TextView = itemView.findViewById(R.id.tv_miles_in)
        private val dailyMilesOut: TextView = itemView.findViewById(R.id.tv_miles_out)
        private val dailyHours: TextView = itemView.findViewById(R.id.tv_hours)
        private val dailyHoursPay: TextView = itemView.findViewById(R.id.tv_hours_pay)
        private val dailyMiles: TextView = itemView.findViewById(R.id.tv_miles)
        private val dailyMilesPay: TextView = itemView.findViewById(R.id.tv_miles_pay)
        private val dailyInitialStops: TextView = itemView.findViewById(R.id.tv_initial_stops)
        private val dailyActualStops: TextView = itemView.findViewById(R.id.tv_actual_stops)
        private val dailyInitialPkgs: TextView = itemView.findViewById(R.id.tv_initial_packages)
        private val dailyActualPkgs: TextView = itemView.findViewById(R.id.tv_actual_packages)

        var dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.US)
            .withZone(ZoneId.systemDefault())

        @SuppressLint("SetTextI18n")
        fun bind(daily: Daily?) {
            val dailyClickListener = OnClickListener {
                listener.onDailyClick(daily!!)
            }
            val dailyLongClickListener = View.OnLongClickListener {
                listener.onDailyLongClick(daily!!)
                true
            }
            itemView.setOnClickListener(dailyClickListener)
            itemView.setOnLongClickListener(dailyLongClickListener)
            dailyDate.text = dateFormatter.format(daily?.createdAt)
            val tp = daily?.getTotalPay().toString()
            dailyPay.text = "$$tp"
            dailyTimeIn.text = daily?.getTimeIn()
            dailyTimeOut.text = daily?.getTimeOut()
            val mIn = daily?.milesIn
            val mOut = daily?.milesOut
            dailyMilesIn.text = mIn.toString()
            dailyMilesOut.text = mOut.toString()


            dailyHours.text = daily?.getHoursWorked()
            val hp = daily?.getHourspay()
            dailyHoursPay.text = "$$hp"
            val miles = mOut?.minus(mIn!!)
            dailyMiles.text = miles.toString()
            val mp = daily?.getMilespay()
            dailyMilesPay.text = "$$mp"
            dailyInitialStops.text = daily?.initialStops.toString()
            dailyActualStops.text = daily?.actualStops.toString()
            dailyInitialPkgs.text = daily?.initialPkgs.toString()
            dailyActualPkgs.text = daily?.actualPkgs.toString()
        }

        companion object {
            fun create(parent: ViewGroup, listener: DailyClickListener): DailyViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.daily_view, parent, false)
                return DailyViewHolder(view, listener)
            }
        }

        class DailyComparator : DiffUtil.ItemCallback<Daily>() {
            override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return true //oldItem.item == newItem.item && oldItem.value == newItem.value
            }
        }
    }

    fun listenWith(listener: DailyClickListener) {
        mListener = listener
    }

    interface DailyClickListener {
        fun onDailyClick(daily: Daily)
        fun onDailyLongClick(daily: Daily)
    }
}