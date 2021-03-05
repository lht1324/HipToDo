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