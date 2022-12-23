package com.jk.thymecard

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant
import kotlin.math.floor

@Entity(tableName = "dailies")
data class Daily(
    @PrimaryKey val id: Long,
    var dateD: Int,
    var dateM: Int,
    var dateY: Int,
    var timeInH: Int,
    var timeInM: Int,
    var timeOutH: Int,
    var timeOutM: Int,
    var timeWorked: Double,
    var hoursPay: Double,
    var milesIn: Int,
    var milesOut: Int,
    var milesPay: Double,
    var initialStops: Int,
    var actualStops: Int,
    var initialPkgs: Int,
    var actualPkgs: Int,
    var createdAt: Instant,
    var updatedAt: Instant
) {
    @Ignore
    private val df = DecimalFormat("#,##0.00")

    constructor() : this(
        System.currentTimeMillis(),
        26, 11, 1970,
        0, 0,
        0, 0,
        0.0, 0.0,
        0, 0,
        0.0,
        0, 0, 0, 0,
        Instant.now(),
        Instant.now()
    )

    fun getTimeIn(): String {
        val h = if (timeInH < 10) "0$timeInH" else "$timeInH"
        val m = if (timeInM < 10) "0$timeInM" else "$timeInM"
        return "$h:$m"
    }

    fun getTimeOut(): String {
        val h = if (timeOutH < 10) "0$timeOutH" else "$timeOutH"
        val m = if (timeOutM < 10) "0$timeOutM" else "$timeOutM"
        return "$h:$m"
    }

    fun getHoursWorked(): String {
        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.CEILING
        if (timeInH == 0 &&
            timeOutH == 0)
            return "0.0"
        val tDiff = getMinutesWorked()
        val hDiff = tDiff / 60
        val mDiff = (tDiff % 60)
        val minAsPct = getMinAsPct(mDiff)
        val hString = if (hDiff < 10) {
            "0$hDiff"
        } else {
            "$hDiff"
        }
        val mString = if (mDiff < 10) {
            "0$mDiff"
        } else {
            "$mDiff"
        }
        return "$hString:$mString ($hString.$minAsPct)"
    }

    private fun getMinutesWorked(): Int {
        val timeInByMinutes = timeInH * 60 + timeInM
        val timeOutByMinutes = timeOutH * 60 + timeOutM
        return timeOutByMinutes - timeInByMinutes
    }

    private fun getMinAsPct(min: Int): Int {
        return floor(min.div(60.0) * 100).toInt()
    }

//    fun getMiles(): Int {
//        return milesOut.minus(milesIn)
//    }

    fun getHourspay(): Double {
        val minutes = getMinutesWorked()
        return df.format((minutes.div(60.0) * 22.0)).toDouble()
    }

    fun getMilespay(): Double {
        return df.format(milesOut.minus(milesIn).times(0.625)).toDouble()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    fun getTotalPay(): Double {
        val h = getHourspay()
        val m = getMilespay()
        return df.format(h.plus(m)).toDouble()
    }
}