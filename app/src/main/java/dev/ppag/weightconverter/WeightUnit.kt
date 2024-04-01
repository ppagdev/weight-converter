package dev.ppag.weightconverter

// weight units used in the app
val options = listOf(
    WeightUnit("Human", 150.0),
    WeightUnit("Wolverine", 13.0)
)

class WeightUnit(val name: String, private val weight: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return (this.weight * amount) / targetUnit.weight
    }
}