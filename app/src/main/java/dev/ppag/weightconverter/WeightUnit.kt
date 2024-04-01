package dev.ppag.weightconverter

val Wolverine = WeightUnit("Wolverine", 5.9)
val Human = WeightUnit("Human", 68.0)



class WeightUnit(val name: String, val weight: Double) {
    fun convertTo(targetUnit: WeightUnit, amount: Double): Double {
        return this.weight / (targetUnit.weight * amount)
    }
}