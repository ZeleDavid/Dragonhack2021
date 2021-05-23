package si.dragonhack.petpal.data.models

import java.io.Serializable
import java.util.*

data class Reminder(val name: String, val place: String, val date: String): Serializable {
}