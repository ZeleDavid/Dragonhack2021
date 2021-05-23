package si.dragonhack.petpal.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_pet.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.api.DogFactApi
import si.dragonhack.petpal.api.TheDogApi


class AddPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        sex_toggle.setSingleSelection(true)
        sex_toggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (checkedId == R.id.female_button) {
                Log.i("SEX", "female")
            } else {
                Log.i("SEX", "male")
            }
        }
        add_pet_button.setOnClickListener {
            addPet()
        }


        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val result: Intent = result.data!!
                    Picasso.get().load(result.data).into(imageView2)

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

    fun addPet() {

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