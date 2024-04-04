package dev.ppag.weightconverter

// weight units used in the app
val options = listOf(
    WeightUnit("Kilogram", 1.0),
    WeightUnit("Gram", 0.001),
    WeightUnit("Pound", 0.4535924),
    WeightUnit("Cat", 4.535924),
    WeightUnit("Human", 62.0),
    WeightUnit("Banana", 0.118),
    WeightUnit("Egg", 0.057),
    WeightUnit("2007 Honda Civic", 1205.195),
    WeightUnit("Sonic the Hedgehog", 35.0),
).sortedBy { it.name }

class WeightUnit(val name: String, private val weight /* Kg */: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return (this.weight * amount) / targetUnit.weight
    }
}