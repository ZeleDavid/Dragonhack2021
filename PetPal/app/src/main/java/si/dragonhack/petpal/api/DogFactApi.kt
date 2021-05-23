package si.dragonhack.petpal.api

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import si.dragonhack.petpal.data.models.Fact
import si.dragonhack.petpal.data.models.TDA_dog_info

class DogFactApi(var queue: RequestQueue) {
    val base_url= "https://dog-facts-api.herokuapp.com"
    val facts_url ="${base_url}/api/v1/resources/dogs/all"

    fun handle_getFacts(callback: (fact: List<Fact>) -> Unit) {
        val url = facts_url
        val req = object : StringRequest(Method.GET, url, Response.Listener<String> { response ->
            val facts: List<Fact> =
                Gson().fromJson(response, Array<Fact>::class.java).toList()
            callback(facts)
            Log.i("TEST", facts.toString())

        }, Response.ErrorListener {
            //TODO: ERROR handler
            print(it)
        }){

        }
        queue.add(req)
    }
}