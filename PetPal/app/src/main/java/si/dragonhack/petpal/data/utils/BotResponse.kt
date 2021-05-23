package si.dragonhack.petpal.data.utils

import android.util.Log
import androidx.lifecycle.ViewModelProviders
import si.dragonhack.petpal.data.FirebaseDatabase
import si.dragonhack.petpal.data.models.PetSymptom
import si.dragonhack.petpal.data.utils.Constants.OPEN_GOOGLE
import si.dragonhack.petpal.data.utils.Constants.OPEN_SEARCH
import si.dragonhack.petpal.data.viewmodel.PetSymptomViewModel
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {
    lateinit var petSymptomViewModel: PetSymptomViewModel

    var somethingWrong: Boolean = false
    var allSymptomsEntered: Boolean = false

    var enteredSymptoms: ArrayList<String> = ArrayList()

    var allSymptoms: ArrayList<String> = ArrayList()

    var allPetSymptoms: ArrayList<PetSymptom> = ArrayList()

    val stemmer = Stemmer()

    fun basicResponses(_message: String): String {
        val random = (0..2).random()
        val message = stemmer.stem(_message.toLowerCase())
        Log.d("TAG",message)

        return when {

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Hey!"
                    2 -> "Buongiorno!"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            !somethingWrong && (message.contains("sick") || message.contains("wrong") || message.contains("hurt"))
            -> {
                somethingWrong = true
                "What symptoms are you noticing on your pet?"
            }

            somethingWrong && message.contains("that's al")-> {
                somethingWrong = false
                Log.d("TAG", enteredSymptoms.toString())
                mostProbableCause(message)
            }

            else -> {
                when (somethingWrong && isSymptom(message)) {
                    true -> "If those are all the symptoms type 'That's all'. Otherwise list another."
                    false -> {
                        //When the programme doesn't understand...
                        when (random) {
                            0 -> "I don't understand..."
                            1 -> "Try asking me something different"
                            2 -> "Idk"
                            else -> "error"
                        }
                    }
                }
            }
        }
    }

    fun isSymptom(message: String): Boolean{
        for (symptom in allSymptoms) {
            val strs = symptom.split(";").toTypedArray()
            for(_i in strs){
                val i = stemmer.stem(_i.toLowerCase())
                if(message.contains(i)){
                    enteredSymptoms.add(i)
                    return true
                }
            }
        }
        return false
    }

    fun mostProbableCause(message: String): String{
        var possiblePetSymptoms = Array<ArrayList<PetSymptom>>(enteredSymptoms.size) { ArrayList<PetSymptom>() }

        var i = 0
        for (symptom in enteredSymptoms) {
            possiblePetSymptoms[i].addAll(ArrayList(allPetSymptoms.filter { item ->
                item.symptoms.any { stemmer.stem(it.toLowerCase()) == symptom }
            }))
            i++
            Log.d("TAG",possiblePetSymptoms.toString())
        }

        var maxPs = 0
        var selectedPetSymptom = PetSymptom()
        for (_symptom in possiblePetSymptoms) {
            for (symptom in _symptom) {
                if(symptom.symptoms.size>maxPs){
                    selectedPetSymptom=symptom
                    maxPs=symptom.symptoms.size
                }
                //val maxObject: PetSymptom = symp.maxBy { it.size }
            }
            //val maxObject: PetSymptom = symp.maxBy { it.size }
        }

        Log.d("TAG",selectedPetSymptom.toString())

        if(selectedPetSymptom != null){
            //return "There is a high posibility of your pet having "+possiblePetSymptoms[0].name+" , you should "+possiblePetSymptoms[0].action
            return "There is a high posibility of your pet having "+selectedPetSymptom.name+" , you should "+ selectedPetSymptom.action
        }
        else{
            return "We could not identify the problem with your pet. Perhaps you should consider taking it to the vet as soon as possible."
        }

    }
}