package si.dragonhack.petpal.data.models

import java.io.Serializable

data class Pet (
    var image: String = "",
    var name: String = "",
    var breed: String = "",
    var weight: Double = 0.0,
    var height: Double = 0.0,
    var age: Int = 0,
    var sex: String = ""
): Serializable{
    override fun toString(): String {
        return "Pet(image='$image', name='$name', breed='$breed', weight=$weight, height=$height, age=$age, sex='$sex')"
    }
}