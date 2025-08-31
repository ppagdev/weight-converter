package dev.ppag.weightconverter

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.edit

fun saveWeightUnits(context: Context, units: List<WeightUnit>) {
    val sharedPreferences = context.getSharedPreferences("weight_units", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        val json = Json.encodeToString(units)
        putString("units", json)
    }
}

fun loadWeightUnits(context: Context): MutableList<WeightUnit> {
    val sharedPreferences = context.getSharedPreferences("weight_units", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("units", null)
    return if (json != null) {
        Json.decodeFromString(json)
    } else {
        mutableListOf(
            WeightUnit("Kilogram", 1.0),
            WeightUnit("Gram", 0.001),
            WeightUnit("Pound", 0.4535924)
        )
    }
}