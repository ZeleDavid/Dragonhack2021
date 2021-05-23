package si.dragonhack.petpal.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.api.TheDogApi
import si.dragonhack.petpal.data.models.Fact
import si.dragonhack.petpal.data.viewmodel.DashboardViewModel
import si.dragonhack.petpal.data.viewmodel.YourPetViewmodel
import si.dragonhack.petpal.ui.adapter.CircularPagerIndicatorDecoration
import si.dragonhack.petpal.ui.adapter.DogFactsAdapter
import si.dragonhack.petpal.ui.adapter.YourPetDataAdapter


class DashboardFragment : Fragment() {

    private lateinit var queue: RequestQueue

    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var yourPetViewModel: YourPetViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        yourPetViewModel =
            ViewModelProviders.of(this).get(YourPetViewmodel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yourPetViewModel.getDogFacts().observe(viewLifecycleOwner, Observer {
            Log.i("FACT", it.count().toString())
            showFacts(it)
        })
    }

    private fun showFacts(facts: List<Fact>){
        val factsAdapter = DogFactsAdapter(facts)
        var recycler = facts_list
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = factsAdapter
    }
}