package dev.ppag.weightconverter

// weight units used in the app
val options = listOf(
    WeightUnit("Kilograms", 1.0),
    WeightUnit("Grams", 0.001),
    WeightUnit("Pounds", 0.4535924),
    WeightUnit("Cats", 4.535924),
    WeightUnit("Humans", 62.0),
    WeightUnit("Bananas", 0.118),
).sortedBy { it.name }

class WeightUnit(val name: String, private val weight /* Kg */: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return (this.weight * amount) / targetUnit.weight
    }
}