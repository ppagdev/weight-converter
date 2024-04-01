package dev.ppag.weightconverter

// weight units used in the app
val options = listOf(
    WeightUnit("Kg", 1.0),
    WeightUnit("Grams", 0.001),
    WeightUnit("Pounds", 0.4535924),
    WeightUnit("Cat", 4.535924),
    WeightUnit("Human Male", 62.0),
    WeightUnit("Banana", 0.118),
)

class WeightUnit(val name: String, private val weight /* Kg */: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return (this.weight * amount) / targetUnit.weight
    }
}