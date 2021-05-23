package si.dragonhack.petpal.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fact_item_layout.view.*
import kotlinx.android.synthetic.main.your_pet_list_item.view.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.models.Fact
import si.dragonhack.petpal.data.models.Pet

class DogFactsAdapter(list: List<Fact>): RecyclerView.Adapter<DogFactsViewHolder>(){
    val factsList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogFactsViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fact_item_layout, parent, false)
        return DogFactsViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return factsList.size
    }

    override fun onBindViewHolder(holder: DogFactsViewHolder, position: Int) {
        holder.bind(factsList[position])
    }
}
class DogFactsViewHolder(val view: View): RecyclerView.ViewHolder(view){
    fun bind(fact: Fact){
        view.fact_text.text = fact.fact
    }
}