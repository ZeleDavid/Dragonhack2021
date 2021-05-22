package si.dragonhack.petpal.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import si.dragonhack.petpal.R
import si.dragonhack.petpal.api.TheDogApi
import si.dragonhack.petpal.data.viewmodel.DashboardViewModel


class DashboardFragment : Fragment() {

    private lateinit var queue: RequestQueue

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        //TODO TDA calls example
        queue = Volley.newRequestQueue(this.context)
        val tda = TheDogApi(queue)
        tda.handle_selected_breed("beagle")
        tda.handle_allBreeds()

        return root
    }

}