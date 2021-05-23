package si.dragonhack.petpal.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.add_reminder.*
import si.dragonhack.petpal.R

class AddReminderBottomSheet : BottomSheetDialogFragment(){

    companion object {

        const val TAG = "CustomBottomSheetDialogFragment"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_reminder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_reminder_button.setOnClickListener {
            //handle click event
            Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()

        }

    }
}