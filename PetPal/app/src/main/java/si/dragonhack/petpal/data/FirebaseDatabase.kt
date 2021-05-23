package si.dragonhack.petpal.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import si.dragonhack.petpal.data.models.PetSymptom

object FirebaseDatabase {
    lateinit var ctx: Context
    private lateinit var database: DatabaseReference
    lateinit var sharedPref: SharedPreferences

    init {
        database = FirebaseDatabase.getInstance().reference
    }

    fun getAllPets(): MutableLiveData<List<PetSymptom>>{
        val mutableData = MutableLiveData<List<PetSymptom>>()
        val rootRef =
            FirebaseDatabase.getInstance().reference
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listek: ArrayList<PetSymptom> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val pet:PetSymptom = PetSymptom()

                    pet.action = ds.child("action").value.toString()
                    pet.name = ds.child("name").value.toString()
                    for (dsNested in ds.child("symptoms").children) {
                        pet.symptoms.add(dsNested.value.toString())
                    }
                    for (dsNested in ds.child("bodyParts").children) {
                        pet.bodyParts.add(dsNested.value.toString())
                    }

                    listek.add(pet)
                }
                mutableData.value=listek

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        rootRef.addListenerForSingleValueEvent(eventListener)
        return mutableData
    }

}