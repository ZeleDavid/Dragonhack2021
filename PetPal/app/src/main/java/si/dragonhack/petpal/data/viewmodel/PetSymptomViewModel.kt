package si.dragonhack.petpal.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import si.dragonhack.petpal.data.FirebaseDatabase
import si.dragonhack.petpal.data.models.PetSymptom

class PetSymptomViewModel : ViewModel() {
    private lateinit var pets: MutableLiveData<List<PetSymptom>>
    private val firebaseDatabase = FirebaseDatabase

    fun getPets(): MutableLiveData<List<PetSymptom>> {
        if(!::pets.isInitialized){
            loadPets()
        }
        return pets
    }
    private fun loadPets(){
        this.pets = firebaseDatabase.getAllPets()
    }
}