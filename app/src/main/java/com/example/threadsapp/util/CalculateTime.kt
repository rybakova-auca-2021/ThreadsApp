package com.example.threadsapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CalculateTime {
    companion object {
        fun calculateTimeDifference(timestamp: String?): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val postTime = dateFormat.parse(timestamp)
                val currentTime = Date()
                val diffMillis = currentTime.time - postTime.time

                val seconds = diffMillis / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val days = hours / 24

                return when {
                    days >= 1 -> "${days.toInt()}d"
                    hours >= 1 -> "${hours.toInt()}h"
                    else -> "${minutes.toInt()}m"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
