package com.overeasy.hiptodo.model

import android.util.Log
import androidx.room.TypeConverter
import java.util.*

class Converters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun fromTimestamp(value: Long?): Calendar? {
            return if (value != null) {
                val date = GregorianCalendar()
                date.timeInMillis = value
                date
            } else null
            // 일단 켜지긴 하는데 다시 켰을 때 날짜를 못 읽어온다
            // 저장되는 게 잘못된 거 아냐?
            /* val date = GregorianCalendar()
            date.timeInMillis = value!! // NPE
            return if (value != null) date else null */
        }
        @JvmStatic
        @TypeConverter
        fun toTimestamp(date: Calendar?): Long? {
            return date?.timeInMillis
        }
        private fun println(data: String) {
            Log.d("Converters", data)
        }
    }
}