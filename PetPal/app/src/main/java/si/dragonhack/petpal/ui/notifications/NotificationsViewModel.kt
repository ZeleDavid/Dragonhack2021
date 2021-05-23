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

    fun getReminders(): MutableLiveData<List<Reminder>>{
        if(!::reminders.isInitialized){
            loadReminders()
        }
        return reminders
    }

    private fun loadReminders() {

    }
}