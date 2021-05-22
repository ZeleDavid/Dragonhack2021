package si.dragonhack.petpal.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_add_pet.*
import si.dragonhack.petpal.R

class AddPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        sex_toggle.setSingleSelection(true)
        sex_toggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(checkedId == R.id.female_button){
                Log.i("SEX", "female")
            }
            else{
                Log.i("SEX", "male")
            }
        }
        add_pet_button.setOnClickListener {
            addPet()
        }
    }
    fun addPet(){

    }
}