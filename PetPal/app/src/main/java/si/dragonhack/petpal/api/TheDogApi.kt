package si.dragonhack.petpal.api

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import si.dragonhack.petpal.data.models.TDA_dog_info

// All functions handling The Dog API service
class TheDogApi(
    private val queue: RequestQueue
) {
    private val api_key = "a88d9dc2-e28c-4c72-a8ea-03c9cc7c98e9";
    private val base_link = "https://api.thedogapi.com/v1"
    private val breeds_link = "${base_link}/breeds"
    private val breeds_search_link = "${breeds_link}/search"

    fun handle_selected_breed(selected_breed_type: String) {
        val url = breeds_search_link
        val req = object : StringRequest(Method.GET, url + "?q=${selected_breed_type}",
            Response.Listener<String> { response ->
                //transform to datatype
                val dogInfo: List<TDA_dog_info> =
                    Gson().fromJson(response, Array<TDA_dog_info>::class.java).toList()
                val selectedDog = dogInfo[0]
                print(selectedDog)
                //TODO: Put this data somewhere

            }, Response.ErrorListener {
                //TODO: ERROR handler
                print(it)
            }) {
            //            override fun getBody(): ByteArray {
//                return super.getBody()
//            }
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

}