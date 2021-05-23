package si.dragonhack.petpal.data.models

data class PetStateComparison(val difference: Double, val keyText: String, val isAverage: Boolean) {
}
data class PetStateWeightHeight(val weight: PetStateComparison, val height: PetStateComparison) {
}