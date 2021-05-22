package si.dragonhack.petpal.data.models


data class TDA_dog_info(
    var bred_for: String,
    var breed_group: String,
    var height: Dimensions,
    var weight: Dimensions,
    var id: Int,
    var image: Image,
    var life_span: String,
    var name: String,
    var origin: String,
    var temperament: String
)

data class Dimensions(
    var imperial: String,
    var metric: String)

data class Image (
    val height: Long,
    val id: String,
    val url: String,
    val width: Long
)