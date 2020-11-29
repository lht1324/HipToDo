package com.overeasy.hiptodo.model

import androidx.room.TypeConverter
import java.util.*

class Converters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun fromTimestamp(value: Long?): Calendar? {
            val date = GregorianCalendar()
            date.timeInMillis = value!!
            return if (value != null) date else null
        }
        @JvmStatic
        @TypeConverter
        fun toTimestamp(date: Calendar?): Long? {
            return date?.timeInMillis
        }
    }
}