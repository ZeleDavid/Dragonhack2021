package si.dragonhack.petpal.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import si.dragonhack.petpal.data.models.Pet

class YourPetViewmodel: ViewModel() {
    private lateinit var selectedPet: MutableLiveData<Pet>
    private lateinit var yourPets: MutableLiveData<List<Pet>>

    fun getYourPets(): MutableLiveData<List<Pet>> {
        if(!::yourPets.isInitialized){
            loadYourPets();
        }
        return yourPets
    }
    fun getSelectedPet(): MutableLiveData<Pet> {
        if(!::selectedPet.isInitialized){
            loadYourPets()
        }
        return selectedPet
    }
    fun setSelectedPet(index: Int) {
        var pet = yourPets.value?.get(index)
        selectedPet.value = pet
    }
    private fun setSelectedPet(pet: Pet){
        this.selectedPet.value = pet
    }
    private fun loadYourPets(){
        //this.nearbyRestaurants = firebaseDatabase.returnNearbyRestaurants()
        val pets = MutableLiveData<List<Pet>>()
        val pet1 = Pet(" ", "Miki", "Beagle", 25.5, 35.5, 2, "M")
        val pet2 = Pet(" ", "Fifika", "Shih-tzu", 6.5, 25.5, 5, "F")
        val pet3 = Pet(" ", "Floki", "Bulldog", 35.5, 45.5, 9, "M")
        pets.value = arrayListOf(pet1, pet2, pet3)
        this.yourPets = pets
        this.selectedPet = MutableLiveData<Pet>(pet1)
    }
}