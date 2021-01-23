package com.overeasy.hiptodo

import com.overeasy.hiptodo.databinding.ActivityMainBinding
import com.skydoves.rainbow.Rainbow
import com.skydoves.rainbow.RainbowOrientation
import com.skydoves.rainbow.contextColor
import java.util.*
import kotlin.properties.Delegates

class BackgroundMaker(private val binding: ActivityMainBinding) {
    fun compareTime() {
        // 6 : 30, 6 : 40, 6 : 50, 7 : 00, 18 : 30, 18 : 40, 18 : 50, 19 : 00
        val calendar: Calendar = GregorianCalendar()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // 6 : 00 ~ 6 : 59
        when (hour) {
            6 -> {
                when (minute) {
                    in 0..14 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.dawn1_1)
                            + contextColor(R.color.dawn1_2)
                            + contextColor(R.color.dawn1_3)
                            + contextColor(R.color.dawn1_4)
                            + contextColor(R.color.dawn1_5)
                            + contextColor(R.color.dawn1_6)
                            + contextColor(R.color.dawn1_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 15..29 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.dawn2_1)
                            + contextColor(R.color.dawn2_2)
                            + contextColor(R.color.dawn2_3)
                            + contextColor(R.color.dawn2_4)
                            + contextColor(R.color.dawn2_5)
                            + contextColor(R.color.dawn2_6)
                            + contextColor(R.color.dawn2_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 30..44 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.dawn3_1)
                            + contextColor(R.color.dawn3_2)
                            + contextColor(R.color.dawn3_3)
                            + contextColor(R.color.dawn3_4)
                            + contextColor(R.color.dawn3_5)
                            + contextColor(R.color.dawn3_6)
                            + contextColor(R.color.dawn3_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 45..59 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.dawn4_1)
                            + contextColor(R.color.dawn4_2)
                            + contextColor(R.color.dawn4_3)
                            + contextColor(R.color.dawn4_4)
                            + contextColor(R.color.dawn4_5)
                            + contextColor(R.color.dawn4_6)
                            + contextColor(R.color.dawn4_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                }
            }
            // 7 : 00 ~ 17 : 59
            in 7..17 -> {
                Rainbow(binding.constraintLayout).palette {
                    + contextColor(R.color.day_1)
                    + contextColor(R.color.day_2)
                    + contextColor(R.color.day_3)
                    + contextColor(R.color.day_4)
                    + contextColor(R.color.day_5)
                    + contextColor(R.color.day_6)
                    + contextColor(R.color.day_7)
                }.background(orientation = RainbowOrientation.BOTTOM_TOP)
            }
            // 18 : 00 ~ 18 : 59
            18 -> {
                when (minute) {
                    in 0..14 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.sunset1_1)
                            + contextColor(R.color.sunset1_2)
                            + contextColor(R.color.sunset1_3)
                            + contextColor(R.color.sunset1_4)
                            + contextColor(R.color.sunset1_5)
                            + contextColor(R.color.sunset1_6)
                            + contextColor(R.color.sunset1_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 15..29 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.sunset2_1)
                            + contextColor(R.color.sunset2_2)
                            + contextColor(R.color.sunset2_3)
                            + contextColor(R.color.sunset2_4)
                            + contextColor(R.color.sunset2_5)
                            + contextColor(R.color.sunset2_6)
                            + contextColor(R.color.sunset2_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 30..44 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.sunset3_1)
                            + contextColor(R.color.sunset3_2)
                            + contextColor(R.color.sunset3_3)
                            + contextColor(R.color.sunset3_4)
                            + contextColor(R.color.sunset3_5)
                            + contextColor(R.color.sunset3_6)
                            + contextColor(R.color.sunset3_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                    in 45..59 -> {
                        Rainbow(binding.constraintLayout).palette {
                            + contextColor(R.color.sunset4_1)
                            + contextColor(R.color.sunset4_2)
                            + contextColor(R.color.sunset4_3)
                            + contextColor(R.color.sunset4_4)
                            + contextColor(R.color.sunset4_5)
                            + contextColor(R.color.sunset4_6)
                            + contextColor(R.color.sunset4_7)
                        }.background(orientation = RainbowOrientation.BOTTOM_TOP)
                    }
                }
            }
            // 19 : 00 ~ 5 : 59
            else -> {
                Rainbow(binding.constraintLayout).palette {
                    + contextColor(R.color.night_1)
                    + contextColor(R.color.night_2)
                    + contextColor(R.color.night_3)
                    + contextColor(R.color.night_4)
                    + contextColor(R.color.night_5)
                    + contextColor(R.color.night_6)
                    + contextColor(R.color.night_7)
                }.background(orientation = RainbowOrientation.BOTTOM_TOP)
            }
        }
    }
}