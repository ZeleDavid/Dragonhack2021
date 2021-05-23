package si.dragonhack.petpal.ui.notifications

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.add_reminder.*
import kotlinx.android.synthetic.main.add_reminder.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.add_reminder_button
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.models.Reminder
import si.dragonhack.petpal.ui.adapter.DogFactsAdapter
import si.dragonhack.petpal.ui.adapter.ReminderAdapter
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsViewModel.getReminders().observe(viewLifecycleOwner, Observer {
            val remindersAdapter = ReminderAdapter(it)
            var recycler = reminders_list
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = remindersAdapter
        })

        add_reminder_button.setOnClickListener {
            val reminder_bottom_sheet = layoutInflater.inflate(R.layout.add_reminder, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(reminder_bottom_sheet)
            dialog.show()
            reminder_bottom_sheet.add_reminder_button.setOnClickListener {
                Log.i("add", "added new reminder")
                notificationsViewModel.addReminder(
                    Reminder(
                        reminder_bottom_sheet.reminder_name.text.toString(),
                        reminder_bottom_sheet.reminder_place.text.toString(),
                        reminder_bottom_sheet.reminder_date.text.toString()
                    )
                )
                dialog.dismiss()
            }
        }
    }
}
