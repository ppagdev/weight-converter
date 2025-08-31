package dev.ppag.weightconverter

import kotlinx.serialization.Serializable


// weight units used in the app
val options = mutableListOf(
    WeightUnit("Kilogram", 1.0),
    WeightUnit("Gram", 0.001),
    WeightUnit("Pound", 0.4535924),
)

@Serializable
class WeightUnit(val name: String, private val weight /* Kg */: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return (this.weight * amount) / targetUnit.weight
    }
}