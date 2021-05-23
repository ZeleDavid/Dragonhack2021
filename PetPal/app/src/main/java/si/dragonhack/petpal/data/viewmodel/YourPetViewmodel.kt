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
import org.json.JSONArray
import si.dragonhack.petpal.api.DogFactApi
import si.dragonhack.petpal.data.models.Fact
import si.dragonhack.petpal.data.models.PetStateWeightHeight
import si.dragonhack.petpal.data.models.TDA_dog_info
import si.dragonhack.petpal.data.utils.Constants
import si.dragonhack.petpal.util.PetStateCalculator
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class YourPetViewmodel(application: Application): AndroidViewModel(application) {
    private lateinit var selectedPet: MutableLiveData<Pet>
    private lateinit var selectedPetComparison: MutableLiveData<PetStateWeightHeight>
    private lateinit var selectedBreedData: MutableLiveData<TDA_dog_info>
    private lateinit var yourPets: MutableLiveData<List<Pet>>
    private lateinit var dogFacts: MutableLiveData<List<Fact>>
    private val context = application.applicationContext

    private var dogApi = TheDogApi(Volley.newRequestQueue(application.applicationContext))
    private var dogFactApi = DogFactApi(Volley.newRequestQueue(application.applicationContext))

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("pets", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun getDogFacts(): MutableLiveData<List<Fact>>{
        if(!::dogFacts.isInitialized){
            loadDogFacts()
        }
        return dogFacts
    }
    fun loadDogFacts(){
        dogFacts = MutableLiveData<List<Fact>>()
        dogFactApi.handle_getFacts(::setDogFacts)
    }
    fun setDogFacts(facts: List<Fact>){
        dogFacts.value = facts
    }

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
    private fun loadYourPets() {
        //this.nearbyRestaurants = firebaseDatabase.returnNearbyRestaurants()
        Constants.DATA_LOCATION
        val pets = MutableLiveData<List<Pet>>()

        val data = getAllPetsData();
        pets.value = data

        if (data.isEmpty()) {
            val pet1 = Pet(" ", "Miki", "Beagle", 25.5, 35.5, 2, "Male")
            val pet2 = Pet(" ", "Fifika", "Labrador", 6.5, 25.5, 5, "Female")
            val pet3 = Pet(" ", "Floki", "Bulldog", 35.5, 45.5, 9, "Male")
            pets.value = arrayListOf(pet1, pet2, pet3)
        }
        this.yourPets = pets
        this.selectedPet = MutableLiveData<Pet>(pets.value!![0])
    }

    private fun getAllPetsData(): List<Pet> {
        val dir = File(context.filesDir, "mydir")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, Constants.DATA_LOCATION)
        if(!file.exists()) run {
            val fw = FileWriter(file)
            fw.append("")
            fw.flush()
            fw.close()
        }
        val fr = FileReader(file)
        var data = ""
        fr.forEachLine {
            data += it
        }
        if (data == "")
            data = "[]"

        val pets_jsonArray = JSONArray(data)//Gson().fromJson(data,JSONArray::class.java)
        var pets = ArrayList<Pet>()
        for(i in 0 until pets_jsonArray.length()){
            pets.add(Gson().fromJson(pets_jsonArray[i].toString(),Pet::class.java))
        }
        return pets
    }
}