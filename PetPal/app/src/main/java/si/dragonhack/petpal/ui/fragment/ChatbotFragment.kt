package si.dragonhack.petpal.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.viewmodel.DashboardViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import si.dragonhack.petpal.data.models.Message
import si.dragonhack.petpal.data.utils.Constants.RECEIVE_ID
import si.dragonhack.petpal.data.utils.Constants.SEND_ID
import si.dragonhack.petpal.data.utils.BotResponse
import si.dragonhack.petpal.data.utils.Constants.OPEN_GOOGLE
import si.dragonhack.petpal.data.utils.Constants.OPEN_SEARCH
import si.dragonhack.petpal.data.utils.Time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chatbot.*
import kotlinx.coroutines.*
import si.dragonhack.petpal.data.FirebaseDatabase
import si.dragonhack.petpal.data.models.PetSymptom
import si.dragonhack.petpal.data.viewmodel.PetSymptomViewModel
import si.dragonhack.petpal.ui.adapter.MessagingAdapter

class ChatbotFragment : Fragment() {

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("David", "Luka", "Primož")

    lateinit var petSymptomViewModel: PetSymptomViewModel

    var pets: ArrayList<PetSymptom> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_chatbot, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()

        clickEvents()

        val random = (0..2).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
    }

    private fun clickEvents() {

        //Send a message
        btn_send.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(getActivity()?.applicationContext)

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }

        FirebaseDatabase.ctx = requireActivity().applicationContext

        petSymptomViewModel = ViewModelProviders.of(this)[PetSymptomViewModel::class.java]

        petSymptomViewModel.getPets().observe(this, Observer { allPets ->
            this.pets=ArrayList(allPets)
            var allSymptoms: ArrayList<String> = ArrayList()
            for (pet in pets) {
                allSymptoms.add(pet.symptoms.joinToString(separator = ";"))
            }

            BotResponse.allSymptoms = allSymptoms
            BotResponse.allPetSymptoms = pets
        })
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                rv_messages.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages?.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}