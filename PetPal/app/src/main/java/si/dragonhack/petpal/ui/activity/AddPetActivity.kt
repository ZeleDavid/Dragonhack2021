package si.dragonhack.petpal.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_pet.*
import org.json.JSONArray
import si.dragonhack.petpal.R
import si.dragonhack.petpal.api.DogFactApi
import si.dragonhack.petpal.api.TheDogApi
import si.dragonhack.petpal.data.models.Pet
import si.dragonhack.petpal.data.utils.Constants
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type


class AddPetActivity : AppCompatActivity() {
    private var sex: String = "Female"
    private var image: Uri = Uri.EMPTY
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        sharedPreferences = application.getSharedPreferences("pets", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        sex_toggle.setSingleSelection(true)
        sex_toggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(checkedId == R.id.female_button){
                Log.i("SEX", "Female")
                sex = "Female"
            }
            else{
                Log.i("SEX", "Male")
                sex = "Male"
            }
        }
        add_pet_button.setOnClickListener {
            addPet(this, Constants.DATA_LOCATION)
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val result: Intent = result.data!!
                    Picasso.get().load(result.data).into(imageView2)
                    image = result.data!!
                    val queue = Volley.newRequestQueue(this)
                    val tda = TheDogApi(queue)
                    if(result.data != null)
                        tda.handle_uploadImage(result.data!!,contentResolver)
                } else {
                    Log.e("PICASSO", "Something is wrong with image loading.")
                }
            }
        imageButton.setOnClickListener {
            addImage(resultLauncher)
        }


    }

    fun fillPredictedBreed(type: String) {
        pet_breed_add.setText(type)
    }

    fun addPet(context: Context, fileName: String) {
        val dir = File(context.getFilesDir(), "mydir")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, fileName)
        if(!file.exists())
        {
            val writer = FileWriter(file)
            writer.append("")
            writer.flush()
            writer.close()
        }

        val reader = FileReader(file)
        var data =""
        reader.forEachLine {
            data += it
        }
        if(data=="")
            data = "[]"

        var allPets = JSONArray(data)

        var gender ="Female"
        if((male_button as MaterialButton).isChecked)
            gender = "Male"

        var image_location=""
        if(imageView2.tag != null)
            image_location = imageView2.tag.toString()


        var newPet = Pet(
            image_location,
            pet_name_add.text.toString(),
            pet_breed_add.text.toString(),
            pet_weight_add.text.toString().toDouble(),
            pet_height_add.text.toString().toDouble(),
            pet_age_add.text.toString().toInt(),
            gender)
        allPets.put(Gson().toJson(newPet))


        val writer = FileWriter(file)
        writer.append(allPets.toString())
        writer.flush()
        writer.close()
    }

    //Handler function, that's called when image button is pressed
    fun addImage(resultLauncher: ActivityResultLauncher<Intent>) {

        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}