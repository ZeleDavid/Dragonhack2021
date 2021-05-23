package si.dragonhack.petpal.ui.notifications

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import si.dragonhack.petpal.data.models.Pet
import si.dragonhack.petpal.data.models.Reminder
import java.lang.reflect.Type

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("pets", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    private lateinit var reminders: MutableLiveData<List<Reminder>>
    val text: LiveData<String> = _text


    fun addReminder(reminder: Reminder){
        var arrayItems = mutableListOf<Reminder>()
        val serializedObject = sharedPreferences.getString("reminders", null);
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type =
                object : TypeToken<List<Pet?>?>() {}.type
            arrayItems = gson.fromJson<Any>(serializedObject, type) as MutableList<Reminder>
        }
        arrayItems.add(reminder)
        editor.putString("reminders", Gson().toJson(arrayItems)).commit()
        reminders = MutableLiveData()
        reminders.value = arrayItems
    }

    fun getReminders(): MutableLiveData<List<Reminder>>{
        if(!::reminders.isInitialized){
            loadReminders()
        }
        return reminders
    }

    private fun loadReminders() {
        val reminders = MutableLiveData<List<Reminder>>()
        val serializedObject = sharedPreferences.getString("reminders", null);
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type =
                object : TypeToken<List<Reminder?>?>() {}.type
            reminders.value = gson.fromJson<Any>(serializedObject, type) as MutableList<Reminder>
        }
        this.reminders = reminders
    }
}