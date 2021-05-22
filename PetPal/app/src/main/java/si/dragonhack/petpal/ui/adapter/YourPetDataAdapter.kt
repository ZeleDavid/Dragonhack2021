package si.dragonhack.petpal.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.your_pet_list_item.view.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.models.Pet

class YourPetDataAdapter(list: List<Pet>): RecyclerView.Adapter<YourPetDataViewHolder>(){
    val petsList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourPetDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.your_pet_list_item, parent, false)
        return YourPetDataViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: YourPetDataViewHolder, position: Int) {
        holder.bind(petsList[position])
    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}
class YourPetDataViewHolder(val view: View): RecyclerView.ViewHolder(view){
    fun bind(pet: Pet){
        view.pet_name.text = pet.name
        view.pet_breed.text = pet.breed
        if(pet.sex == "Female"){
            view.pet_sex_icon.setImageResource(R.drawable.ic_female)
        }
    }
}

public interface OnItemChangedListener<T: YourPetDataViewHolder> {
    /**
     * Called when new item is selected. It is similar to the onScrollEnd of ScrollStateChangeListener, except that it is
     * also called when currently selected item appears on the screen for the first time.
     * viewHolder will be null, if data set becomes empty
     */
    fun onCurrentItemChanged(@Nullable viewHolder: T, adapterPosition: Int);
}