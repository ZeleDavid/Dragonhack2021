package si.dragonhack.petpal.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_home.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.models.Pet
import si.dragonhack.petpal.data.models.PetStateWeightHeight
import si.dragonhack.petpal.data.viewmodel.HomeViewModel
import si.dragonhack.petpal.data.viewmodel.YourPetViewmodel
import si.dragonhack.petpal.ui.activity.AddPetActivity
import si.dragonhack.petpal.ui.adapter.CircularPagerIndicatorDecoration
import si.dragonhack.petpal.ui.adapter.YourPetDataAdapter
import si.dragonhack.petpal.ui.adapter.YourPetDataViewHolder


class HomeFragment : Fragment(), DiscreteScrollView.OnItemChangedListener<YourPetDataViewHolder> {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var yourPetViewModel: YourPetViewmodel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        yourPetViewModel =
                ViewModelProviders.of(this).get(YourPetViewmodel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yourPetViewModel.getYourPets().observe(viewLifecycleOwner, Observer {
            setupYourPetsList(it)
        })
        yourPetViewModel.getSelectedPet().observe(viewLifecycleOwner, Observer {
            updateYourPetInfo(it)
        })
        yourPetViewModel.getSelectedBreedData().observe(viewLifecycleOwner, Observer {
            val temperaments = it.temperament.split(", ")
            showTraits(temperaments)
        })
        yourPetViewModel.getComparison().observe(viewLifecycleOwner, Observer {
            updateComparison(it)
        })

        add_pet_button.setOnClickListener {
            val intent = Intent(this.context, AddPetActivity::class.java)
            startActivity(intent)
        }


    }

    private fun updateComparison(comparison: PetStateWeightHeight){
        Log.i("comp", comparison.weight.difference.toString())
        if(comparison.weight.isAverage){
            weight_difference_text.text = "Average"
            weight_desc.text = "Your pet is normal"
            weight_difference_number.text = ""
            kg.text = ""
        }
        else{
            weight_difference_text.text = comparison.weight.keyText
            weight_difference_number.text = comparison.weight.difference.toString()
            weight_desc.text = "Than average by"
            kg.text = "kg"
        }
        if(comparison.height.isAverage){
            height_difference_text.text = "Average"
            height_desc.text = "Your pet is normal"
            height_difference_number.text = ""
            cm.text = ""
        }
        else{
            height_difference_text.text = comparison.height.keyText
            height_difference_number.text = comparison.height.difference.toString()
            height_desc.text = "Than average by"
            cm.text = "cm"
        }
    }
    private fun updateYourPetInfo(pet: Pet?) {
        pet_height.text = pet?.height.toString()
        pet_weight.text = pet?.weight.toString()
    }
    fun showTraits(traits: List<String>){
        chipsPrograms.removeAllViews()
        for(trait in traits){
            val mchip = this.layoutInflater.inflate(R.layout.pet_trait_chip, null, false) as Chip
            mchip.text = trait
            val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f,
                resources.displayMetrics
            )
            mchip.setPadding(paddingDp.toInt(), 0, paddingDp.toInt(), 0)
            chipsPrograms.addView(mchip)
        }
    }

    fun setupYourPetsList(petsList: List<Pet>){
        val yourPetDataAdapter =
            YourPetDataAdapter(petsList)
        val yourPetsRecyclerView = your_pets_list
        yourPetsRecyclerView.adapter = yourPetDataAdapter
        setDiscreteViewItemTransformation(yourPetsRecyclerView)
        yourPetsRecyclerView.setSlideOnFling(false)
        yourPetsRecyclerView.setOffscreenItems(2);
        yourPetsRecyclerView.addOnItemChangedListener(this);
        yourPetsRecyclerView.addItemDecoration(CircularPagerIndicatorDecoration());
    }
    private fun setDiscreteViewItemTransformation(yourPetsRecyclerView: DiscreteScrollView) {
        yourPetsRecyclerView.setItemTransformer(
            ScaleTransformer
                .Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build()
        )
    }

    override fun onCurrentItemChanged(viewHolder: YourPetDataViewHolder?, adapterPosition: Int) {
        yourPetViewModel.setSelectedPet(adapterPosition)
    }
}