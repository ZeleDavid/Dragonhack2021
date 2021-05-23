package si.dragonhack.petpal.api

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import si.dragonhack.petpal.data.models.TDA_dog_info
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.ByteArrayOutputStream
import java.util.*


// All functions handling The Dog API service
class TheDogApi(
    private val queue: RequestQueue
) {
    private val api_key = "a88d9dc2-e28c-4c72-a8ea-03c9cc7c98e9";
    private val base_link = "https://api.thedogapi.com/v1"
    private val breeds_link = "${base_link}/breeds"
    private val breeds_search_link = "${breeds_link}/search"
    private val upload_image_link = "${base_link}/images/upload"
    private val BOUNDARY = "--UsmiliteSeNasInNasRazglasiteKotZmagovalce"
    private val mimeType = "multipart/form-data;boundary=${BOUNDARY}"

    fun handle_selected_breed(selected_breed_type: String, callback: (dog: TDA_dog_info) -> Unit) {
        val url = breeds_search_link
        val req = object : StringRequest(Method.GET, url + "?q=${selected_breed_type}",
            Response.Listener<String> { response ->
                //transform to datatype
                val dogInfo: List<TDA_dog_info> =
                    Gson().fromJson(response, Array<TDA_dog_info>::class.java).toList()
                if(dogInfo.count() > 0){
                    val selectedDog = dogInfo[0]
                    print(selectedDog)
                    callback(selectedDog)
                }
            }, Response.ErrorListener {
                print(it)
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = api_key
                return headers
            }
        }

        queue.add(req)
    }

    fun handle_allBreeds() {
        val url = breeds_link
        val req = object : StringRequest(Method.GET, url, Response.Listener<String> { response ->
            val dogInfo: List<TDA_dog_info> =
                //transform to datatype
                Gson().fromJson(response, Array<TDA_dog_info>::class.java).toList()
            //TODO: Put this data somewhere

        }, Response.ErrorListener {
            //TODO: ERROR handler
            print(it)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = api_key
                return headers
            }
        }

        queue.add(req)
    }

    fun handle_uploadImage(image_path: Uri, contentResolver: ContentResolver) {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image_path)
        var image_as_string = getStringImage(bitmap)

        val headers = HashMap<String, String>()
        headers["x-api-key"] = api_key

         val request = MultipartRequest(upload_image_link, headers,
             {
            print(it)
             },
             {
                 print(it)
                 }
         )

//        request.addPart(MultipartRequest.FormPart("file", "image.jpg"));
        request.addPart(MultipartRequest.FilePart("file", mimeType, image_path.toString(), image_as_string.toByteArray()))

        queue.add(request)
    }

    //Convert image to String
    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun createPostBody(params: Map<String, String?>?): String? {
        val sbPost = StringBuilder()
        if (params != null) {
            for (key in params.keys) {
                if (params[key] != null) {
                    sbPost.append("\r\n--$BOUNDARY\r\n")
                    sbPost.append("Content-Disposition: form-data; name=\"$key\"\r\n\r\n")
                    sbPost.append(params[key].toString())
                }
            }
        }
        return sbPost.toString()
    }

}