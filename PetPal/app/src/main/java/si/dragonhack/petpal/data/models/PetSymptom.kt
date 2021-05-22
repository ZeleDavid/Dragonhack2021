package si.dragonhack.petpal.data.models

data class PetSymptom(
    var action: String = "",
    var bodyParts: ArrayList<String> = ArrayList(),
    var name: String = "",
    var symptoms: ArrayList<String> = ArrayList()) {

    override fun toString(): String {
        return "PetSymptom(action='$action', bodyParts=$bodyParts, name='$name', symptoms=$symptoms)"
    }
}