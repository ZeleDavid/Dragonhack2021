package si.dragonhack.petpal.util

import si.dragonhack.petpal.data.models.Pet
import si.dragonhack.petpal.data.models.PetStateComparison
import si.dragonhack.petpal.data.models.PetStateWeightHeight
import si.dragonhack.petpal.data.models.TDA_dog_info

class PetStateCalculator {
    companion object {
        fun calculateWeight(average: String, weight: Double): PetStateComparison{
            var averageWeights = average.split(" - ")
            if(weight > averageWeights[1].toDouble()){
                val difference = weight - averageWeights[1].toDouble()
                return PetStateComparison(difference, "Heavier", false)
            }
            else if(weight < averageWeights[0].toDouble()){
                val difference = averageWeights[1].toDouble() - weight
                return PetStateComparison(difference, "Lighter", false)
            }
            else{
                return PetStateComparison(0.0, "Average", true)
            }
        }
        fun calculateHeight(average: String, height: Double): PetStateComparison{
            var averageHeights = average.split(" - ")
            if(height > averageHeights[1].toDouble()){
                val difference = height - averageHeights[1].toDouble()
                return PetStateComparison(difference, "Taller", false)
            }
            else if(height < averageHeights[0].toDouble()){
                val difference = averageHeights[1].toDouble() - height
                return PetStateComparison(difference, "Shorter", false)
            }
            else{
                return PetStateComparison(0.0, "Average", true)
            }
        }
        fun getComparison(pet: Pet, breed: TDA_dog_info): PetStateWeightHeight{
            var weight = calculateWeight(breed.weight.metric, pet.weight)
            var height = calculateHeight(breed.height.metric, pet.height)
            return PetStateWeightHeight(weight, height)
        }
    }

}