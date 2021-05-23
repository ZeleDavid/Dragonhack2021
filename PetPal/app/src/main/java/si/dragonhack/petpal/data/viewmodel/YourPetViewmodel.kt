package si.dragonhack.petpal.data.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import si.dragonhack.petpal.api.TheDogApi
import si.dragonhack.petpal.data.models.Pet
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import si.dragonhack.petpal.data.models.PetStateWeightHeight
import si.dragonhack.petpal.data.models.TDA_dog_info
import si.dragonhack.petpal.util.PetStateCalculator
import java.lang.reflect.Type

class YourPetViewmodel(application: Application): AndroidViewModel(application) {
    private lateinit var selectedPet: MutableLiveData<Pet>
    private lateinit var selectedPetComparison: MutableLiveData<PetStateWeightHeight>
    private lateinit var selectedBreedData: MutableLiveData<TDA_dog_info>
    private lateinit var yourPets: MutableLiveData<List<Pet>>
    private var dogApi = TheDogApi(Volley.newRequestQueue(application.applicationContext))
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("pets", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun getYourPets(): MutableLiveData<List<Pet>> {
        if(!::yourPets.isInitialized){
            loadYourPets();
        }
        return yourPets
    }
    fun getComparison(): MutableLiveData<PetStateWeightHeight> {
        if(!::selectedPetComparison.isInitialized){
            selectedPetComparison = MutableLiveData<PetStateWeightHeight>()
        }
        return selectedPetComparison
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
        Log.i("BREED", pet!!.breed)
        dogApi.handle_selected_breed(pet!!.breed, ::setSelectedBreedData)

    }
    fun getSelectedBreedData(): MutableLiveData<TDA_dog_info>{
        if(!::selectedBreedData.isInitialized){
            selectedBreedData = MutableLiveData<TDA_dog_info>()
        }
        return selectedBreedData
    }
    fun setSelectedBreedData(breed: TDA_dog_info){
        selectedBreedData.value = breed
        selectedPetComparison.value = PetStateCalculator.getComparison(selectedPet.value!!, breed)
    }
    private fun setSelectedPet(pet: Pet){
        this.selectedPet.value = pet
    }
    private fun loadYourPets(){
        //this.nearbyRestaurants = firebaseDatabase.returnNearbyRestaurants()
        val pets = MutableLiveData<List<Pet>>()
        val serializedObject = sharedPreferences.getString("pets", null);
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type =
                object : TypeToken<List<Pet?>?>() {}.type
            pets.value = gson.fromJson<Any>(serializedObject, type) as MutableList<Pet>
        }
        //val pets = MutableLiveData<List<Pet>>()
        val pet1 = Pet(" ", "Miki", "Beagle", 25.5, 35.5, 2, "Male")
        /*val pet2 = Pet(" ", "Fifika", "Labrador", 6.5, 25.5, 5, "Female")
        val pet3 = Pet(" ", "Floki", "Bulldog", 35.5, 45.5, 9, "Male")
        pets.value = arrayListOf(pet1, pet2, pet3)*/
        this.yourPets = pets
        this.selectedPet = MutableLiveData<Pet>(pet1)
    }
}