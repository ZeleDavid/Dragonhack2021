package si.dragonhack.petpal.data.models

data class Pet (
    var image: String = "",
    var name: String = "",
    var breed: String = "",
    var weight: Double = 0.0,
    var height: Double = 0.0,
    var age: Int = 0,
    var sex: String = ""
){
}