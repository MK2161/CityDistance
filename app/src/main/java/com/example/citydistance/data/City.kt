package com.example.citydistance.data

import java.util.*

data class City(
    val country: String,
    val name: String,
    val lat: Double,
    val lng: Double,
)
fun Double?.valueOrDefault(default: Double = 0.0): Double = this ?: default

fun <T : Number> T.formatDistance(): String =
"${String.format(Locale.ENGLISH, "%.2f", this.toDouble())}Km"

